package com.finance.manager.cleanarch.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Unit tests for UserUseCase.
 */
@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private UserUseCase userUseCase;

  @BeforeEach
  void setUp() {
    userUseCase = new UserUseCase(userRepository, passwordEncoder);
  }

  @Test
  @DisplayName("Should create user with valid data")
  void shouldCreateUserWithValidData() {
    String email = "test@example.com";
    String password = "Test@2024";
    String name = "Test User";

    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn("encoded");
    when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

    User user = userUseCase.registerUser(email, password, name);

    assertEquals(email, user.getEmail());
    assertEquals("encoded", user.getPassword());
    assertEquals(name, user.getName());
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("Should throw exception when email already exists")
  void shouldThrowExceptionWhenEmailExists() {
    String email = "test@example.com";
    String password = "Test@2024";
    String name = "Test User";

    when(userRepository.existsByEmail(email)).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userUseCase.registerUser(email, password, name));

    assertEquals("Email already registered", exception.getMessage());

    verify(userRepository).existsByEmail(email);
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("Should throw exception when password is invalid")
  void shouldThrowExceptionWhenPasswordIsInvalid() {
    String email = "test@example.com";
    String password = "short";
    String name = "Test User";

    when(userRepository.existsByEmail(email)).thenReturn(false);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userUseCase.registerUser(email, password, name));

    assertEquals("Password must be at least 8 characters long", exception.getMessage());

    verify(userRepository).existsByEmail(email);
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(User.class));
  }
}
