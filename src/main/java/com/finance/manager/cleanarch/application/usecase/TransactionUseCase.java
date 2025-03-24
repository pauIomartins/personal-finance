package com.finance.manager.cleanarch.application.usecase;

import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for managing transactions.
 * Acts as an application service in the clean architecture.
 */
@Service
@RequiredArgsConstructor
public class TransactionUseCase {

  private final TransactionRepository transactionRepository;

  /**
   * Adds a new transaction.
   * If the transaction is an expense, the amount will be converted to negative.
   * If the transaction is an income, the amount will be converted to positive.
   *
   * @param user the user who owns the transaction
   * @param transaction the transaction to add
   * @return the added transaction
   */
  public Transaction addTransaction(User user, Transaction transaction) {
    validateTransaction(transaction);
    transaction.setUser(user);
    return transactionRepository.save(transaction);
  }

  /**
   * Gets all transactions for a user.
   *
   * @param user the user to get transactions for
   * @return list of transactions
   */
  public List<Transaction> getUserTransactions(User user) {
    return transactionRepository.findByUser(user);
  }

  /**
   * Gets financial summary for a user.
   *
   * @param user the user to get summary for
   * @return financial summary
   */
  public FinancialSummary getFinancialSummary(User user) {
    List<Transaction> transactions = getUserTransactions(user);
    double totalIncome = transactions.stream()
        .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
        .mapToDouble(Transaction::getAmount)
        .sum();
    double totalExpenses = transactions.stream()
        .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
        .mapToDouble(Transaction::getAbsoluteAmount)
        .sum();
    return new FinancialSummary(totalIncome, totalExpenses, totalIncome - totalExpenses);
  }

  private void validateTransaction(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }
    if (transaction.getUser() == null) {
      throw new IllegalArgumentException("Transaction must have a user");
    }
  }

  /**
   * Financial summary record.
   *
   * @param totalIncome total income
   * @param totalExpenses total expenses
   * @param balance current balance
   */
  public record FinancialSummary(double totalIncome, double totalExpenses, double balance) {}
}
