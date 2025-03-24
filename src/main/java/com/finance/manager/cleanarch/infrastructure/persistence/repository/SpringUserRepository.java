package com.finance.manager.cleanarch.infrastructure.persistence.repository;

import com.finance.manager.cleanarch.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for UserEntity.
 */
@Repository
public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  boolean existsByEmail(String email);
}
