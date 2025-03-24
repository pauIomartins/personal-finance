package com.finance.manager.cleanarch.infrastructure.persistence.repository;

import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.domain.repository.UserRepository;
import com.finance.manager.cleanarch.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of the domain UserRepository interface using Spring Data JPA.
 * This adapter translates between domain and persistence models.
 * This class is not designed for extension.
 */
@Repository
public class JpaUserRepository implements UserRepository {
  
  private final SpringUserRepository repository;

  public JpaUserRepository(SpringUserRepository repository) {
    this.repository = repository;
  }

  /**
   * Finds a user by their email address.
   *
   * @param email the email address to search for
   * @return an Optional containing the user if found, empty otherwise
   */
  @Override
  public Optional<User> findByEmail(String email) {
    return repository.findByEmail(email)
        .map(UserEntity::toDomainModel);
  }

  /**
   * Saves a user to the repository.
   *
   * @param user the user to save
   * @return the saved user, converted to domain model
   */
  @Override
  public User save(User user) {
    UserEntity entity = new UserEntity(user);
    return repository.save(entity).toDomainModel();
  }

  /**
   * Finds a user by their ID.
   *
   * @param id the ID to search for
   * @return an Optional containing the user if found, empty otherwise
   */
  @Override
  public Optional<User> findById(Long id) {
    return repository.findById(id)
        .map(UserEntity::toDomainModel);
  }

  /**
   * Checks if a user exists with the given email address.
   *
   * @param email the email address to check
   * @return true if a user exists with the email, false otherwise
   */
  @Override
  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }
}
