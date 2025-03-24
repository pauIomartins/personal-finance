package com.finance.manager.cleanarch.infrastructure.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration for the application.
 * Defines security rules and authentication mechanisms.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Configures security filter chain.
   *
   * @param http the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/register",
                "/login",
                "/h2-console/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/webjars/**"
            )
            .permitAll()
            .anyRequest()
            .authenticated())
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard")
            .failureUrl("/login?error=true"))
        .logout(logout -> logout
            .logoutSuccessUrl("/?logout=true"))
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
        .headers(headers -> headers
            .frameOptions(withDefaults()));

    return http.build();
  }

  /**
   * Configures password encoder.
   *
   * @return the configured PasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
