package com.finance.manager.cleanarch.infrastructure.persistence.repository;

import com.finance.manager.cleanarch.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository interface for TransactionEntity.
 */
@Repository
public interface SpringTransactionRepository extends JpaRepository<TransactionEntity, Long> {

  List<TransactionEntity> findByUserIdOrderByDateDesc(Long userId);

  
  List<TransactionEntity> findByUserId(Long userId);
}
