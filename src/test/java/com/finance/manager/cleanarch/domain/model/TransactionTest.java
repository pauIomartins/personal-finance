package com.finance.manager.cleanarch.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.finance.manager.cleanarch.domain.model.Transaction.TransactionType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Transaction domain model.
 */
class TransactionTest {

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User("test@example.com", "Test@2024", "Test User");
    testUser.setId(1L);
  }

  @Test
  @DisplayName("Should create valid income transaction")
  void createValidIncomeTransaction() {
    Transaction transaction = new Transaction(100.0, "Salary", "Income", TransactionType.INCOME, testUser);
    assertEquals(100.0, transaction.getAmount());
    assertEquals("Salary", transaction.getDescription());
    assertEquals("Income", transaction.getCategory());
    assertEquals(TransactionType.INCOME, transaction.getType());
    assertEquals(testUser, transaction.getUser());
    assertNotNull(transaction.getDate());
  }

  @Test
  @DisplayName("Should create valid expense transaction")
  void createValidExpenseTransaction() {
    Transaction transaction = new Transaction(50.0, "Rent", "Housing", TransactionType.EXPENSE, testUser);
    assertEquals(-50.0, transaction.getAmount());
    assertEquals(50.0, transaction.getAbsoluteAmount());
    assertEquals("Rent", transaction.getDescription());
    assertEquals("Housing", transaction.getCategory());
    assertEquals(TransactionType.EXPENSE, transaction.getType());
    assertEquals(testUser, transaction.getUser());
    assertNotNull(transaction.getDate());
  }

  @Test
  @DisplayName("Should throw exception for zero amount")
  void shouldThrowExceptionForZeroAmount() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(0.0, "Test", "Test", TransactionType.INCOME, testUser));
    assertEquals("Transaction amount cannot be zero", exception.getMessage());
  }

  @Test
  @DisplayName("Should handle negative income amount")
  void shouldHandleNegativeIncomeAmount() {
    Transaction transaction = new Transaction(-100.0, "Salary", "Income", TransactionType.INCOME, testUser);
    assertEquals(100.0, transaction.getAmount());
    assertEquals(100.0, transaction.getAbsoluteAmount());
  }

  @Test
  @DisplayName("Should handle negative expense amount")
  void shouldHandleNegativeExpenseAmount() {
    Transaction transaction = new Transaction(-50.0, "Rent", "Housing", TransactionType.EXPENSE, testUser);
    assertEquals(-50.0, transaction.getAmount());
    assertEquals(50.0, transaction.getAbsoluteAmount());
  }

  @Test
  @DisplayName("Should throw exception for null description")
  void shouldThrowExceptionForNullDescription() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, null, "Test", TransactionType.INCOME, testUser));
    assertEquals("Transaction description cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for empty description")
  void shouldThrowExceptionForEmptyDescription() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, "", "Test", TransactionType.INCOME, testUser));
    assertEquals("Transaction description cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for description too long")
  void shouldThrowExceptionForDescriptionTooLong() {
    String longDescription = "a".repeat(256);
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, longDescription, "Test", TransactionType.INCOME, testUser));
    assertEquals("Transaction description cannot exceed 255 characters", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for null category")
  void shouldThrowExceptionForNullCategory() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, "Test", null, TransactionType.INCOME, testUser));
    assertEquals("Transaction category cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for empty category")
  void shouldThrowExceptionForEmptyCategory() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, "Test", "", TransactionType.INCOME, testUser));
    assertEquals("Transaction category cannot be empty", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for category too long")
  void shouldThrowExceptionForCategoryTooLong() {
    String longCategory = "a".repeat(51);
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, "Test", longCategory, TransactionType.INCOME, testUser));
    assertEquals("Transaction category cannot exceed 50 characters", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for null type")
  void shouldThrowExceptionForNullType() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, "Test", "Test", null, testUser));
    assertEquals("Transaction type cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for null user")
  void shouldThrowExceptionForNullUser() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Transaction(100.0, "Test", "Test", TransactionType.INCOME, null));
    assertEquals("User cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("Should set and get ID")
  void shouldSetAndGetId() {
    Transaction transaction = new Transaction(100.0, "Test", "Test", TransactionType.INCOME, testUser);
    transaction.setId(1L);
    assertEquals(1L, transaction.getId());
  }

  @Test
  @DisplayName("Should set and get date")
  void shouldSetAndGetDate() {
    Transaction transaction = new Transaction(100.0, "Test", "Test", TransactionType.INCOME, testUser);
    LocalDateTime now = LocalDateTime.now();
    transaction.setDate(now);
    assertEquals(now, transaction.getDate());
  }

  @Test
  @DisplayName("Should set and get user")
  void shouldSetAndGetUser() {
    Transaction transaction = new Transaction(100.0, "Test", "Test", TransactionType.INCOME, testUser);
    User newUser = new User("new@example.com", "Test@2024", "New User");
    transaction.setUser(newUser);
    assertEquals(newUser, transaction.getUser());
  }

  @Test
  @DisplayName("Should compare transactions by ID")
  void shouldCompareTransactionsById() {
    Transaction t1 = new Transaction(100.0, "Test", "Test", TransactionType.INCOME, testUser);
    Transaction t2 = new Transaction(100.0, "Test", "Test", TransactionType.INCOME, testUser);
    t1.setId(1L);
    t2.setId(1L);
    assertEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());
  }
}
