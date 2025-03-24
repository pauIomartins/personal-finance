package com.finance.manager.cleanarch.infrastructure.config;

import com.finance.manager.cleanarch.infrastructure.persistence.repository.SpringUserRepository;
import com.finance.manager.cleanarch.infrastructure.security.CustomUserDetailsService;
import com.finance.manager.cleanarch.infrastructure.security.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for tests.
 */
@TestConfiguration
@EnableWebSecurity
@Import(SecurityConfig.class)
public class TestSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(SpringUserRepository userRepository) {
    return new CustomUserDetailsService(userRepository);
  }
}
