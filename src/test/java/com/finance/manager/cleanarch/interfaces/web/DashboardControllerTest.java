package com.finance.manager.cleanarch.interfaces.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.finance.manager.cleanarch.application.usecase.TransactionUseCase;
import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.Transaction.TransactionType;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.infrastructure.persistence.repository.JpaUserRepository;
import com.finance.manager.cleanarch.interfaces.dto.TransactionDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for DashboardController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DashboardControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @MockBean
  private TransactionUseCase transactionUseCase;

  @MockBean
  private JpaUserRepository userRepository;

  private User testUser;
  private List<Transaction> testTransactions;
  private TransactionUseCase.FinancialSummary testSummary;
  private List<TransactionDto> testTransactionDtos;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();

    // Setup test data
    testUser = new User("test@example.com", "Test@2024", "Test User");
    testUser.setId(1L);

    Transaction income = new Transaction(1000.0, "Salary", "Income", TransactionType.INCOME, testUser);
    income.setId(1L);
    income.setDate(LocalDateTime.now());

    Transaction expense = new Transaction(500.0, "Rent", "Housing", TransactionType.EXPENSE, testUser);
    expense.setId(2L);
    expense.setDate(LocalDateTime.now());

    testTransactions = Arrays.asList(income, expense);
    testTransactionDtos = testTransactions.stream()
        .map(TransactionDto::fromDomain)
        .collect(Collectors.toList());
    testSummary = new TransactionUseCase.FinancialSummary(1000.0, 500.0, 500.0);

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
  }

  @Test
  @DisplayName("Should show index page for anonymous user")
  void index_ShouldShowIndexPageForAnonymousUser() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  @DisplayName("Should redirect to dashboard for authenticated user")
  @WithMockUser(username = "test@example.com")
  void index_ShouldRedirectToDashboardForAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/")
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/dashboard"));
  }

  @Test
  @DisplayName("Should show dashboard with transactions and summary")
  @WithMockUser(username = "test@example.com")
  void dashboard_ShouldShowDashboardWithTransactionsAndSummary() throws Exception {
    when(transactionUseCase.getUserTransactions(any(User.class))).thenReturn(testTransactions);
    when(transactionUseCase.getFinancialSummary(any(User.class))).thenReturn(testSummary);

    mockMvc.perform(get("/dashboard")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("dashboard"))
        .andExpect(model().attributeExists("transactions"))
        .andExpect(model().attributeExists("totalIncome"))
        .andExpect(model().attributeExists("totalExpenses"))
        .andExpect(model().attributeExists("balance"))
        .andExpect(model().attribute("transactions", testTransactionDtos))
        .andExpect(model().attribute("totalIncome", testSummary.totalIncome()))
        .andExpect(model().attribute("totalExpenses", testSummary.totalExpenses()))
        .andExpect(model().attribute("balance", testSummary.balance()));
  }

  @Test
  @DisplayName("Should show empty dashboard for new user")
  @WithMockUser(username = "new@example.com")
  void dashboard_ShouldShowEmptyDashboardForNewUser() throws Exception {
    User newUser = new User("new@example.com", "Test@2024", "New User");
    TransactionUseCase.FinancialSummary emptySummary = new TransactionUseCase.FinancialSummary(0.0, 0.0, 0.0);

    when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.of(newUser));
    when(transactionUseCase.getUserTransactions(any(User.class))).thenReturn(List.of());
    when(transactionUseCase.getFinancialSummary(any(User.class))).thenReturn(emptySummary);

    mockMvc.perform(get("/dashboard")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("dashboard"))
        .andExpect(model().attributeExists("transactions"))
        .andExpect(model().attributeExists("totalIncome"))
        .andExpect(model().attributeExists("totalExpenses"))
        .andExpect(model().attributeExists("balance"))
        .andExpect(model().attribute("transactions", List.of()))
        .andExpect(model().attribute("totalIncome", 0.0))
        .andExpect(model().attribute("totalExpenses", 0.0))
        .andExpect(model().attribute("balance", 0.0));
  }

  @Test
  @DisplayName("Should require authentication for dashboard")
  void dashboard_ShouldRequireAuthentication() throws Exception {
    mockMvc.perform(get("/dashboard"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }

  @Test
  @DisplayName("Should show add transaction form")
  @WithMockUser(username = "test@example.com")
  void showAddTransactionForm_ShouldShowForm() throws Exception {
    mockMvc.perform(get("/dashboard/add")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("transaction/add"))
        .andExpect(model().attributeExists("transaction"))
        .andExpect(model().attributeExists("transactionTypes"));
  }

  @Test
  @DisplayName("Should add transaction successfully")
  @WithMockUser(username = "test@example.com")
  void addTransaction_ShouldAddTransactionSuccessfully() throws Exception {
    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(100.0);
    transactionDto.setDescription("Test Transaction");
    transactionDto.setCategory("Test");
    transactionDto.setType(TransactionType.INCOME);

    when(transactionUseCase.addTransaction(any(User.class), any(Transaction.class)))
        .thenReturn(new Transaction(100.0, "Test Transaction", "Test", TransactionType.INCOME, testUser));

    mockMvc.perform(post("/dashboard/add")
            .with(csrf())
            .param("amount", "100.0")
            .param("description", "Test Transaction")
            .param("category", "Test")
            .param("type", "INCOME"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/dashboard"))
        .andExpect(flash().attributeExists("success"));
  }

  @Test
  @DisplayName("Should handle validation errors when adding transaction")
  @WithMockUser(username = "test@example.com")
  void addTransaction_ShouldHandleValidationErrors() throws Exception {
    when(transactionUseCase.addTransaction(any(User.class), any(Transaction.class)))
        .thenThrow(new IllegalArgumentException("Invalid transaction"));

    mockMvc.perform(post("/dashboard/add")
            .with(csrf())
            .param("amount", "-1.0")
            .param("description", "")
            .param("category", "")
            .param("type", "INCOME"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/dashboard/add"))
        .andExpect(flash().attributeExists("error"));
  }

  @Test
  @DisplayName("Should require authentication for adding transaction")
  void addTransaction_ShouldRequireAuthentication() throws Exception {
    mockMvc.perform(post("/dashboard/add")
            .with(csrf())
            .param("amount", "100.0")
            .param("description", "Test")
            .param("category", "Test")
            .param("type", "INCOME"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }
}
