package com.finance.manager.cleanarch.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

  @ParameterizedTest
  @ValueSource(strings = {
    "",
    "test@",
    "@example.com",
    "test.example.com",
    "test@example",
    "test@@example.com",
    "test@example..com",
    ".@gmail.com",           // email starting with dot
    ".test@example.com",     // local part starting with dot
    "test.@example.com"      // local part ending with dot
  })
  @DisplayName("Should reject invalid emails")
  void isValidEmail_WithInvalidEmails_ReturnsFalse(String invalidEmail) {
    assertThrows(IllegalArgumentException.class, () -> new User(invalidEmail, "Test@2024", "Test User"));
  }

  @Test
  @DisplayName("Should validate password format")
  void isValidPassword_WithValidPassword_ReturnsTrue() {
    User user = new User("test@example.com", "Test@2024", "Test User");
    assertTrue(user.isValidPassword());
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "test",              // too short
    "testpassword",      // no uppercase, no special char, no number
    "TESTPASSWORD",      // no lowercase, no special char, no number
    "Test123",           // no special char
    "test@pass",         // no uppercase, no number
    "TEST@PASS",         // no lowercase, no number
    "testpass1",         // no uppercase, no special char
    "Test Pass@1"        // contains whitespace
  })
  @DisplayName("Should reject invalid passwords")
  void isValidPassword_WithInvalidPasswords_ReturnsFalse(String invalidPassword) {
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", invalidPassword, "Test User"));
  }

  @Test
  @DisplayName("Should reject null password")
  void isValidPassword_WithNullPassword_ReturnsFalse() {
    assertThrows(IllegalArgumentException.class, () -> new User("test@example.com", null, "Test User"));
  }

  @Test
  @DisplayName("Should return email pattern for validation")
  void getEmailPattern_ShouldReturnValidRegexPattern() {
    String emailPattern = User.getEmailPattern();
    
    // Verify pattern is not null or empty
    assertTrue(emailPattern != null && !emailPattern.isEmpty());
    
    // Test pattern with valid emails
    assertTrue("test@example.com".matches(emailPattern));
    assertTrue("user.name@domain.co.uk".matches(emailPattern));
    assertTrue("firstname.lastname@example.com".matches(emailPattern));
    
    // Test pattern with invalid emails
    assertFalse("invalid".matches(emailPattern));
    assertFalse("invalid@".matches(emailPattern));
    assertFalse("@example.com".matches(emailPattern));
    assertFalse("test@invalid".matches(emailPattern));
  }
  
  @Test
  @DisplayName("Should return password requirements description")
  void getPasswordRequirements_ShouldReturnDescription() {
    String requirements = User.getPasswordRequirements();
    
    // Verify requirements contain important information
    assertTrue(requirements.contains("Password must be at least"));
    assertTrue(requirements.contains("digit"));
    assertTrue(requirements.contains("lowercase"));
    assertTrue(requirements.contains("uppercase"));
    assertTrue(requirements.contains("special character"));
    
    // Verify no format exception with special characters
    assertDoesNotThrow(() -> User.getPasswordRequirements());
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
    assertTrue(requirements.contains("whitespace"));
  }

  @Test
  @DisplayName("Should return password pattern for validation")
  void getPasswordPattern_ShouldReturnValidRegexPattern() {
    String passwordPattern = User.getPasswordPattern();
    
    // Verify pattern is not null or empty
    assertTrue(passwordPattern != null && !passwordPattern.isEmpty());
    
    // Test pattern with valid passwords
    assertTrue("Test1234!".matches(passwordPattern));
    assertTrue("Password123@".matches(passwordPattern));
    assertTrue("Secure$789Pass".matches(passwordPattern));
    
    // Test pattern with invalid passwords
    assertFalse("password".matches(passwordPattern)); // No uppercase, no digit, no special char
    assertFalse("PASSWORD123".matches(passwordPattern)); // No lowercase, no special char
    assertFalse("Password123".matches(passwordPattern)); // No special char
    assertFalse("Password!".matches(passwordPattern)); // No digit
    assertFalse("12345678!".matches(passwordPattern)); // No letter
    assertFalse("Pass word1!".matches(passwordPattern)); // Contains space
    assertFalse("Short1!".matches(passwordPattern)); // Too short
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

  @Test
  @DisplayName("Should accept valid name with spaces")
  void shouldAcceptValidNameWithSpaces() {
    User user = new User("test@example.com", "Test@2024", "John Doe");
    assertEquals("John Doe", user.getName());
  }

  @Test
  @DisplayName("Should accept valid name with special characters")
  void shouldAcceptValidNameWithSpecialCharacters() {
    User user = new User("test@example.com", "Test@2024", "O'Connor-Smith");
    assertEquals("O'Connor-Smith", user.getName());
  }

  @Test
  @DisplayName("Should validate multiple valid passwords")
  void shouldValidateMultipleValidPasswords() {
    String[] validPasswords = {
      "Test@2024",
      "Complex@Pass123",
      "MyP@ssw0rd",
      "Str0ng!Pass"
    };

    for (String password : validPasswords) {
      User user = new User("test@example.com", password, "Test User");
      assertTrue(user.isValidPassword(), "Password should be valid: " + password);
    }
  }
}
