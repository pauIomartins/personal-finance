package com.finance.manager.cleanarch.infrastructure.persistence.repository;

import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.domain.repository.TransactionRepository;
import com.finance.manager.cleanarch.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the domain TransactionRepository interface using Spring Data JPA.
 * This adapter translates between domain and persistence models.
 */
@Repository
public class JpaTransactionRepository implements TransactionRepository {

  private final SpringTransactionRepository repository;

  public JpaTransactionRepository(SpringTransactionRepository repository) {
    this.repository = repository;
  }

  /**
   * Finds a transaction by its ID.
   *
   * @param id the ID to search for
   * @return an Optional containing the transaction if found, empty otherwise
   */
  @Override
  public Optional<Transaction> findById(Long id) {
    return repository.findById(id)
        .map(TransactionEntity::toDomainModel);
  }

  /**
   * Saves a transaction to the repository.
   *
   * @param transaction the transaction to save
   * @return the saved transaction, converted to domain model
   */
  @Override
  public Transaction save(Transaction transaction) {
    TransactionEntity entity = new TransactionEntity(transaction);
    return repository.save(entity).toDomainModel();
  }

  /**
   * Finds all transactions for a user, ordered by date descending.
   *
   * @param user the user whose transactions to find
   * @return list of transactions ordered by date descending
   */
  @Override
  public List<Transaction> findByUserOrderByDateDesc(User user) {
    return repository.findByUserIdOrderByDateDesc(user.getId())
        .stream()
        .map(TransactionEntity::toDomainModel)
        .toList();
  }

  /**
   * Finds all transactions for a user.
   *
   * @param user the user whose transactions to find
   * @return list of transactions
   */
  @Override
  public List<Transaction> findByUser(User user) {
    return repository.findByUserId(user.getId())
        .stream()
        .map(TransactionEntity::toDomainModel)
        .toList();
  }
}
