package com.finance.manager.cleanarch.domain.repository;

import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Transaction entities in the domain layer.
 * This interface follows the Clean Architecture principle of depending on abstractions.
 */
public interface TransactionRepository {

  /**
   * Finds a transaction by its ID.
   *
   * @param id the transaction ID to search for
   * @return an Optional containing the transaction if found
   */
  Optional<Transaction> findById(Long id);

  /**
   * Saves a transaction entity.
   *
   * @param transaction the transaction to save
   * @return the saved transaction
   */
  Transaction save(Transaction transaction);

  /**
   * Finds all transactions belonging to a specific user, ordered by date descending.
   *
   * @param user the user whose transactions to find
   * @return list of transactions belonging to the user, ordered by date descending
   */
  List<Transaction> findByUserOrderByDateDesc(User user);

  /**
   * Finds all transactions for a specific user.
   *
   * @param user the user whose transactions to find
   * @return list of all transactions for the user
   */
  List<Transaction> findByUser(User user);
}
