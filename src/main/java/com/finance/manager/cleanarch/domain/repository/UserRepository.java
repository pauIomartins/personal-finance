package com.finance.manager.cleanarch.domain.repository;

import com.finance.manager.cleanarch.domain.model.User;

import java.util.Optional;

/**
 * Repository interface for managing User entities in the domain layer.
 * This interface follows the Clean Architecture principle of depending on abstractions.
 */
public interface UserRepository {

  /**
   * Finds a user by their email address.
   *
   * @param email the email address to search for
   * @return an Optional containing the user if found
   */
  Optional<User> findByEmail(String email);

  /**
   * Saves a user entity.
   *
   * @param user the user to save
   * @return the saved user
   */
  User save(User user);

  /**
   * Finds a user by their ID.
   *
   * @param id the user ID to search for
   * @return an Optional containing the user if found
   */
  Optional<User> findById(Long id);

  /**
   * Checks if a user with the given email exists.
   *
   * @param email the email to check
   * @return true if a user with the email exists, false otherwise
   */
  boolean existsByEmail(String email);
}
