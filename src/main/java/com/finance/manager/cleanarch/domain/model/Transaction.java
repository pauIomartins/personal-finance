package com.finance.manager.cleanarch.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Domain model representing a financial transaction.
 * This class is final and not designed for extension.
 */
@Getter
@EqualsAndHashCode(of = "id")
public final class Transaction {

  @Setter
  private Long id;
  private double amount;
  private String description;
  private String category;
  private TransactionType type;
  @Setter
  private LocalDateTime date;
  @Setter
  private User user;

  /**
   * Creates a new transaction with the specified details.
   *
   * @param amount the transaction amount (must be non-zero)
   * @param description the transaction description
   * @param category the transaction category
   * @param type the transaction type (INCOME or EXPENSE)
   * @param user the user who owns this transaction
   * @throws IllegalArgumentException if description/category is invalid
   */
  public Transaction(double amount, 
                     final String description, 
                     final String category, 
                     final TransactionType type,
                     final User user) {
    validateAmount(amount);
    validateDescription(description);
    validateCategory(category);
    if (type == null) {
      throw new IllegalArgumentException("Transaction type cannot be null");
    }
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }

    this.amount = type == TransactionType.EXPENSE ? -Math.abs(amount) : Math.abs(amount);
    this.description = description;
    this.category = category;
    this.type = type;
    this.user = user;
    this.date = LocalDateTime.now();
  }

  /**
   * Gets the absolute value of the transaction amount.
   *
   * @return the absolute transaction amount
   */
  public double getAbsoluteAmount() {
    return Math.abs(amount);
  }

  private void validateAmount(double amount) {
    if (amount == 0) {
      throw new IllegalArgumentException("Transaction amount cannot be zero");
    }
  }

  private void validateDescription(String description) {
    if (description == null || description.trim().isEmpty()) {
      throw new IllegalArgumentException("Transaction description cannot be empty");
    }
    if (description.length() > 255) {
      throw new IllegalArgumentException("Transaction description cannot exceed 255 characters");
    }
  }

  private void validateCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
      throw new IllegalArgumentException("Transaction category cannot be empty");
    }
    if (category.length() > 50) {
      throw new IllegalArgumentException("Transaction category cannot exceed 50 characters");
    }
  }

  /**
   * Transaction types.
   */
  public enum TransactionType {
    INCOME,
    EXPENSE
  }
}
