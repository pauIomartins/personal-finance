package com.finance.manager.cleanarch.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.Transaction.TransactionType;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.domain.repository.TransactionRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for TransactionUseCase.
 */
@ExtendWith(MockitoExtension.class)
class TransactionUseCaseTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Captor
  private ArgumentCaptor<Transaction> transactionCaptor;

  private TransactionUseCase transactionUseCase;
  private User testUser;

  @BeforeEach
  void setUp() {
    transactionUseCase = new TransactionUseCase(transactionRepository);
    testUser = new User("test@example.com", "Test@2024", "Test User");
    testUser.setId(1L);
  }

  @Test
  @DisplayName("Should add income transaction with positive amount")
  void addTransaction_WithPositiveIncome_ShouldKeepPositive() {
    Transaction income = new Transaction(100.0, "Salary", "Income", TransactionType.INCOME, testUser);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

    Transaction saved = transactionUseCase.addTransaction(testUser, income);

    verify(transactionRepository).save(transactionCaptor.capture());
    Transaction captured = transactionCaptor.getValue();
    assertEquals(100.0, captured.getAmount());
    assertEquals(100.0, saved.getAmount());
  }

  @Test
  @DisplayName("Should add expense transaction with negative amount")
  void addTransaction_WithPositiveExpense_ShouldConvertToNegative() {
    Transaction expense = new Transaction(50.0, "Rent", "Housing", TransactionType.EXPENSE, testUser);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

    Transaction saved = transactionUseCase.addTransaction(testUser, expense);

    verify(transactionRepository).save(transactionCaptor.capture());
    Transaction captured = transactionCaptor.getValue();
    assertEquals(-50.0, captured.getAmount());
    assertEquals(-50.0, saved.getAmount());
  }

  @Test
  @DisplayName("Should handle negative expense amount")
  void addTransaction_WithNegativeExpense_ShouldKeepNegative() {
    Transaction expense = new Transaction(-50.0, "Rent", "Housing", TransactionType.EXPENSE, testUser);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

    Transaction saved = transactionUseCase.addTransaction(testUser, expense);

    verify(transactionRepository).save(transactionCaptor.capture());
    Transaction captured = transactionCaptor.getValue();
    assertEquals(-50.0, captured.getAmount());
    assertEquals(-50.0, saved.getAmount());
  }

  @Test
  @DisplayName("Should handle negative income amount")
  void addTransaction_WithNegativeIncome_ShouldConvertToPositive() {
    Transaction income = new Transaction(-100.0, "Salary", "Income", TransactionType.INCOME, testUser);
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

    Transaction saved = transactionUseCase.addTransaction(testUser, income);

    verify(transactionRepository).save(transactionCaptor.capture());
    Transaction captured = transactionCaptor.getValue();
    assertEquals(100.0, captured.getAmount());
    assertEquals(100.0, saved.getAmount());
  }

  @Test
  @DisplayName("Should get user transactions")
  void getUserTransactions_ShouldReturnUserTransactions() {
    List<Transaction> transactions = Arrays.asList(
        new Transaction(100.0, "Salary", "Income", TransactionType.INCOME, testUser),
        new Transaction(50.0, "Rent", "Housing", TransactionType.EXPENSE, testUser)
    );
    when(transactionRepository.findByUser(testUser)).thenReturn(transactions);

    List<Transaction> result = transactionUseCase.getUserTransactions(testUser);

    assertEquals(transactions, result);
    verify(transactionRepository).findByUser(testUser);
  }

  @Test
  @DisplayName("Should calculate financial summary")
  void getFinancialSummary_ShouldCalculateCorrectly() {
    List<Transaction> transactions = Arrays.asList(
        new Transaction(100.0, "Salary", "Income", TransactionType.INCOME, testUser),
        new Transaction(50.0, "Rent", "Housing", TransactionType.EXPENSE, testUser)
    );
    when(transactionRepository.findByUser(testUser)).thenReturn(transactions);

    TransactionUseCase.FinancialSummary summary = transactionUseCase.getFinancialSummary(testUser);

    assertEquals(100.0, summary.totalIncome());
    assertEquals(50.0, summary.totalExpenses());
    assertEquals(50.0, summary.balance());
    verify(transactionRepository).findByUser(testUser);
  }

  @Test
  @DisplayName("Should throw exception for null transaction")
  void addTransaction_WithNullTransaction_ShouldThrowException() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> transactionUseCase.addTransaction(testUser, null));
    assertEquals("Transaction cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception for null user")
  void addTransaction_WithNullUser_ShouldThrowException() {
    Transaction transaction = new Transaction(100.0, "Test", "Test", TransactionType.INCOME, testUser);
    transaction.setUser(null);
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> transactionUseCase.addTransaction(testUser, transaction));
    assertEquals("Transaction must have a user", exception.getMessage());
  }
}
