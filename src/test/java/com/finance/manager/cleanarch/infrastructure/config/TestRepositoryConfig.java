package com.finance.manager.cleanarch.infrastructure.config;

import com.finance.manager.cleanarch.domain.repository.UserRepository;
import com.finance.manager.cleanarch.infrastructure.persistence.repository.JpaUserRepository;
import com.finance.manager.cleanarch.infrastructure.persistence.repository.SpringUserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Repository configuration for tests.
 */
@TestConfiguration
public class TestRepositoryConfig {

  @Bean
  public UserRepository userRepository(SpringUserRepository springUserRepository) {
    return new JpaUserRepository(springUserRepository);
  }
}
