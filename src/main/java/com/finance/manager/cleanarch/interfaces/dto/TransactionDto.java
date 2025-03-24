package com.finance.manager.cleanarch.interfaces.dto;

import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.Transaction.TransactionType;
import com.finance.manager.cleanarch.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for Transaction domain model.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionDto {

  private Long id;
  private double amount;
  private String description;
  private String category;
  private TransactionType type;
  private LocalDateTime date;

  /**
   * Converts this DTO to a domain model.
   *
   * @param user the user who owns the transaction
   * @return the domain model
   */
  public Transaction toDomain(User user) {
    Transaction transaction = new Transaction(amount, description, category, type, user);
    transaction.setId(id);
    if (date != null) {
      transaction.setDate(date);
    }
    return transaction;
  }

  /**
   * Creates a DTO from a domain model.
   *
   * @param transaction the domain model
   * @return the DTO
   */
  public static TransactionDto fromDomain(Transaction transaction) {
    return new TransactionDto(
        transaction.getId(),
        transaction.getAmount(),
        transaction.getDescription(),
        transaction.getCategory(),
        transaction.getType(),
        transaction.getDate()
    );
  }
}
