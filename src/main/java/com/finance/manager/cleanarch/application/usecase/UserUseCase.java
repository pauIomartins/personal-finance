package com.finance.manager.cleanarch.application.usecase;

import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Application service for managing user-related business operations.
 * This class orchestrates the domain objects and implements use case logic.
 */
@Service
public class UserUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Registers a new user in the system.
   *
   * @param email user's email address
   * @param password user's password
   * @param name user's name
   * @return the registered user
   * @throws IllegalArgumentException if email is already taken or validation fails
   */
  public User registerUser(String email, String password, String name) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already registered");
    }

    User user = new User(email, password, name);
    
    if (!user.isValidEmail()) {
      throw new IllegalArgumentException("Please provide a valid email address");
    }

    if (!user.isValidPassword()) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
    }

    user.setPassword(passwordEncoder.encode(password));
    return userRepository.save(user);
  }

  /**
   * Finds a user by their email address.
   *
   * @param email the email address to search for
   * @return the user if found
   * @throws IllegalArgumentException if user is not found
   */
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}
