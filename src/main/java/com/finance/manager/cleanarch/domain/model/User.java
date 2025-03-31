package com.finance.manager.cleanarch.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain model representing a user.
 * This class is final and not designed for extension.
 */
@Getter
@EqualsAndHashCode(of = "id")
public final class User {

  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final String PASSWORD_PATTERN = 
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
  private static final String EMAIL_PATTERN =
      "^[A-Za-z0-9][A-Za-z0-9+_.-]*[A-Za-z0-9]@"         // Local part with valid start/end
      + "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?"      // Domain first part
      + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$";  // Domain parts

  @Setter
  private Long id;
  private String email;
  @Setter
  private String password;
  private String name;

  /**
   * Creates a new user with the specified details.
   *
   * @param email the user's email (must be valid)
   * @param password the user's password (must be encoded)
   * @param name the user's name
   */
  public User(String email, String password, String name) {
    validateEmail(email);
    validatePassword(password);
    validateName(name);

    this.email = email;
    this.password = password;
    this.name = name;
  }

  /**
   * Validates if the email address is in a valid format.
   *
   * @return true if email is valid, false otherwise
   */
  public boolean isValidEmail() {
    try {
      validateEmail(this.email);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Validates if the password meets the minimum requirements:
   * - At least 8 characters long
   * - Contains at least one digit
   * - Contains at least one lowercase letter
   * - Contains at least one uppercase letter
   * - Contains at least one special character (@#$%^&+=!)
   * - No whitespace allowed.
   *
   * @return true if password is valid, false otherwise
   */
  public boolean isValidPassword() {
    try {
      validatePassword(this.password);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Returns a human-readable description of password requirements.
   *
   * @return string describing password requirements
   */
  public static String getPasswordRequirements() {
    return String.format(
        "Password must be at least %d characters long and contain:"
        + "\n- At least one digit"
        + "\n- At least one lowercase letter"
        + "\n- At least one uppercase letter"
        + "\n- At least one special character (@#$%%^&+=!)"
        + "\n- No whitespace allowed",
        MIN_PASSWORD_LENGTH);
  }

  /**
   * Returns the email pattern used for validation.
   *
   * @return the email pattern regex
   */
  public static String getEmailPattern() {
    return EMAIL_PATTERN;
  }

  /**
   * Returns the password pattern used for validation.
   *
   * @return the password pattern regex
   */
  public static String getPasswordPattern() {
    return PASSWORD_PATTERN;
  }

  private void validateEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }
    if (!email.matches(EMAIL_PATTERN)) {
      throw new IllegalArgumentException("Invalid email format");
    }
  }

  private void validatePassword(String password) {
    if (password == null || password.trim().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be empty");
    }
    if (password.length() < MIN_PASSWORD_LENGTH) {
      throw new IllegalArgumentException("Password must be at least 8 characters long");
    }
    if (!password.matches(PASSWORD_PATTERN)) {
      throw new IllegalArgumentException("Password must meet complexity requirements: "
          + "digit, lowercase, uppercase, special char, no spaces");
    }
  }

  private void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
    if (name.length() > 100) {
      throw new IllegalArgumentException("Name cannot exceed 100 characters");
    }
  }
}
