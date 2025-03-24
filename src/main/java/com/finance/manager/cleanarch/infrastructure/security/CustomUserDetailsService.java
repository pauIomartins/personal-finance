package com.finance.manager.cleanarch.infrastructure.security;

import com.finance.manager.cleanarch.infrastructure.persistence.repository.SpringUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of UserDetailsService that loads user-specific data.
 * This service translates our custom User entity to Spring Security's UserDetails.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final SpringUserRepository userRepository;

  public CustomUserDetailsService(SpringUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
  }
}
