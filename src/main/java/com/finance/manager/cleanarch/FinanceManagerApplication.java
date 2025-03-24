package com.finance.manager.cleanarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Personal Finance Manager.
 * Uses Spring Boot for auto-configuration and component scanning.
 */
@SpringBootApplication
public class FinanceManagerApplication {

  /**
   * Main entry point for the application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(FinanceManagerApplication.class, args);
  }
}
