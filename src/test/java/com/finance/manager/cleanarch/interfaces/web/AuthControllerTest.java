package com.finance.manager.cleanarch.interfaces.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.finance.manager.cleanarch.application.usecase.UserUseCase;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.infrastructure.persistence.entity.UserEntity;
import com.finance.manager.cleanarch.infrastructure.persistence.repository.SpringUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

/**
 * Unit tests for AuthController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @MockBean
  private UserUseCase userUseCase;

  @MockBean
  private SpringUserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Test
  @DisplayName("Should show login page")
  @WithAnonymousUser
  void login_ShouldShowLoginPage() throws Exception {
    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"));
  }

  @Test
  @DisplayName("Should show login page with error message")
  @WithAnonymousUser
  void login_WithError_ShouldShowLoginPageWithError() throws Exception {
    mockMvc.perform(get("/login").param("error", "true"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("error"));
  }

  @Test
  @DisplayName("Should authenticate and redirect to dashboard")
  void login_WithValidCredentials_ShouldAuthenticateAndRedirectToDashboard() throws Exception {
    String email = "john@example.com";
    String password = "Test@2024";
    String name = "John Doe";

    User user = new User(email, passwordEncoder.encode(password), name);
    UserEntity userEntity = new UserEntity(user);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

    mockMvc.perform(post("/login")
            .with(csrf())
            .param("username", email)
            .param("password", password))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/dashboard"));
  }

  @Test
  @DisplayName("Should redirect to login with error")
  void login_WithInvalidCredentials_ShouldRedirectToLoginWithError() throws Exception {
    mockMvc.perform(post("/login")
            .with(csrf())
            .param("username", "invalid@example.com")
            .param("password", "wrongpassword"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error=true"));
  }

  @Test
  @DisplayName("Should show register page")
  @WithAnonymousUser
  void register_ShouldShowRegisterPage() throws Exception {
    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("user"))
        .andExpect(model().attributeExists("passwordRequirements"));
  }

  @Test
  @DisplayName("Should display register form")
  @WithAnonymousUser
  void registerForm_ShouldDisplayRegisterForm() throws Exception {
    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("user"))
        .andExpect(model().attributeExists("passwordRequirements"))
        .andExpect(model().attributeExists("emailPattern"));
  }

  @Test
  @DisplayName("Should pass password pattern to register form")
  @WithAnonymousUser
  void registerForm_ShouldPassPasswordPatternToModel() throws Exception {
    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("passwordPattern"))
        .andExpect(model().attribute("passwordPattern", User.getPasswordPattern()));
  }

  @Test
  @DisplayName("Should validate password confirmation match")
  @WithAnonymousUser
  void register_WithPasswordMismatch_ShouldReturnToRegisterForm() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("email", "test@example.com")
            .param("password", "Password1!")
            .param("passwordConfirmation", "DifferentPassword1!")
            .param("name", "Test User"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("error"))
        .andExpect(model().attribute("error", "Passwords do not match"));
  }

  @Test
  @DisplayName("Should pass password pattern to model on password mismatch")
  @WithAnonymousUser
  void register_WithPasswordMismatch_ShouldPassPasswordPatternToModel() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("email", "test@example.com")
            .param("password", "Password1!")
            .param("passwordConfirmation", "DifferentPassword1!")
            .param("name", "Test User"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("passwordPattern"))
        .andExpect(model().attribute("passwordPattern", User.getPasswordPattern()));
  }

  @Test
  @DisplayName("Should register user with matching passwords")
  @WithAnonymousUser
  void register_WithMatchingPasswords_ShouldRedirectToLogin() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("email", "test@example.com")
            .param("password", "Password1!")
            .param("passwordConfirmation", "Password1!")
            .param("name", "Test User"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?registered=true"));
  }

  @Test
  @DisplayName("Should register user and render login page")
  void register_WithValidData_ShouldRegisterAndRedirect() throws Exception {
    // Não criamos um objeto User real, apenas mockamos o comportamento do userUseCase
    when(userUseCase.registerUser(anyString(), anyString(), anyString()))
        .thenReturn(new User("test@example.com", "Password1!", "Test User"));

    mockMvc.perform(post("/register")
            .with(csrf())
            .param("email", "test@example.com")
            .param("password", "Password1!")
            .param("passwordConfirmation", "Password1!")
            .param("name", "Test User"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?registered=true"));
  }

  @Test
  @DisplayName("Should show error on invalid registration data")
  void register_WithInvalidData_ShouldRedirectWithError() throws Exception {
    when(userUseCase.registerUser(anyString(), anyString(), anyString()))
        .thenThrow(new IllegalArgumentException("Invalid data"));

    mockMvc.perform(post("/register")
            .with(csrf())
            .param("email", "test@example.com")
            .param("password", "Password1!")
            .param("passwordConfirmation", "Password1!")
            .param("name", "Test User"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("error"));
  }

  @Test
  @DisplayName("Should pass password pattern to model on validation error")
  @WithAnonymousUser
  void register_WithValidationError_ShouldPassPasswordPatternToModel() throws Exception {
    when(userUseCase.registerUser(anyString(), anyString(), anyString()))
        .thenThrow(new IllegalArgumentException("Email already registered"));
    
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("email", "existing@example.com")
            .param("password", "Password1!")
            .param("passwordConfirmation", "Password1!")
            .param("name", "Test User"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("passwordPattern"))
        .andExpect(model().attribute("passwordPattern", User.getPasswordPattern()));
  }

  @Test
  @DisplayName("Should redirect to home after logout")
  @WithMockUser
  void logout_ShouldRedirectToHome() throws Exception {
    mockMvc.perform(post("/logout")
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/?logout=true"));
  }
}
