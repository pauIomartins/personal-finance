package com.finance.manager.cleanarch.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for User domain model.
 */
class UserTest {

  @Test
  @DisplayName("Should create valid user")
  void createValidUser() {
    User user = new User("test@example.com", "Test@2024", "Test User");
    assertEquals("test@example.com", user.getEmail());
    assertEquals("Test@2024", user.getPassword());
    assertEquals("Test User", user.getName());
  }

  @Test
  @DisplayName("Should validate email format")
  void isValidEmail_WithValidEmail_ReturnsTrue() {
    User user = new User("test@example.com", "Test@2024", "Test User");
    assertTrue(user.isValidEmail());
  }

  @Test
  @DisplayName("Should reject invalid emails")
  void isValidEmail_WithInvalidEmails_ReturnsFalse() {
    assertThrows(IllegalArgumentException.class, () -> new User("", "Test@2024", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("test@", "Test@2024", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("@example.com", "Test@2024", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("test.example.com", "Test@2024", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("test@example", "Test@2024", "Test User"));
  }

  @Test
  @DisplayName("Should validate password format")
  void isValidPassword_WithValidPassword_ReturnsTrue() {
    User user = new User("test@example.com", "Test@2024", "Test User");
    assertTrue(user.isValidPassword());
  }

  @Test
  @DisplayName("Should reject invalid passwords")
  void isValidPassword_WithInvalidPasswords_ReturnsFalse() {
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", "test", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", "testpassword", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", "TESTPASSWORD", "Test User"));
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", "Test123", "Test User"));
  }

  @Test
  @DisplayName("Should reject null password")
  void isValidPassword_WithNullPassword_ReturnsFalse() {
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", null, "Test User"));
  }

  @Test
  @DisplayName("Should provide password requirements")
  void getPasswordRequirements_ShouldReturnRequirements() {
    String requirements = User.getPasswordRequirements();
    assertTrue(requirements.contains("8 characters"));
    assertTrue(requirements.contains("digit"));
    assertTrue(requirements.contains("lowercase"));
    assertTrue(requirements.contains("uppercase"));
    assertTrue(requirements.contains("special character"));
  }

  @Test
  @DisplayName("Should throw exception for null email")
  void shouldThrowExceptionForNullEmail() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User(null, "Test@2024", "Test User"));
    assertEquals("Email cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for empty email")
  void shouldThrowExceptionForEmptyEmail() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("", "Test@2024", "Test User"));
    assertEquals("Email cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for invalid email format")
  void shouldThrowExceptionForInvalidEmailFormat() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("notanemail", "Test@2024", "Test User"));
    assertEquals("Invalid email format", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for null password")
  void shouldThrowExceptionForNullPassword() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("test@example.com", null, "Test User"));
    assertEquals("Password cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for empty password")
  void shouldThrowExceptionForEmptyPassword() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("test@example.com", "", "Test User"));
    assertEquals("Password cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for short password")
  void shouldThrowExceptionForShortPassword() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("test@example.com", "short", "Test User"));
    assertEquals("Password must be at least 8 characters long", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for null name")
  void shouldThrowExceptionForNullName() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("test@example.com", "Test@2024", null));
    assertEquals("Name cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for empty name")
  void shouldThrowExceptionForEmptyName() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("test@example.com", "Test@2024", ""));
    assertEquals("Name cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for name too long")
  void shouldThrowExceptionForNameTooLong() {
    String longName = "a".repeat(101);
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new User("test@example.com", "Test@2024", longName));
    assertEquals("Name cannot exceed 100 characters", exception.getMessage());
  }
}
