package com.finance.manager.cleanarch.infrastructure.persistence.entity;

import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.Transaction.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity for Transaction persistence.
 * This class adapts the domain Transaction model to the database schema.
 * This class is final and not designed for extension.
 */
@Entity
@Getter
@Setter
@Table(name = "transactions")
public final class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @Column(nullable = false)
  private LocalDateTime date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  /**
   * Default constructor.
   */
  public TransactionEntity() {
  }

  /**
   * Constructor with Transaction domain model.
   *
   * @param transaction the Transaction domain model to create this entity from
   */
  public TransactionEntity(Transaction transaction) {
    this.id = transaction.getId();
    this.amount = transaction.getAmount();
    this.description = transaction.getDescription();
    this.category = transaction.getCategory();
    this.type = transaction.getType();
    this.date = transaction.getDate();
    if (transaction.getUser() != null) {
      this.user = new UserEntity(transaction.getUser());
    }
  }

  /**
   * Converts this entity to a domain model.
   *
   * @return the Transaction domain model
   */
  public Transaction toDomainModel() {
    Transaction transaction = new Transaction(
        amount, description, category, type, user.toDomainModel());
    transaction.setId(id);
    transaction.setDate(date);
    return transaction;
  }
}
