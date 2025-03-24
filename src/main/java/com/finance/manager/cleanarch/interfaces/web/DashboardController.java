package com.finance.manager.cleanarch.interfaces.web;

import com.finance.manager.cleanarch.application.usecase.TransactionUseCase;
import com.finance.manager.cleanarch.application.usecase.TransactionUseCase.FinancialSummary;
import com.finance.manager.cleanarch.domain.model.Transaction;
import com.finance.manager.cleanarch.domain.model.Transaction.TransactionType;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.infrastructure.persistence.repository.JpaUserRepository;
import com.finance.manager.cleanarch.interfaces.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for dashboard-related operations.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

  private final TransactionUseCase transactionUseCase;
  private final JpaUserRepository userRepository;

  /**
   * Shows the index page.
   *
   * @param principal the authenticated user
   * @return the view name or redirect
   */
  @GetMapping("/")
  public String index(Principal principal) {
    return principal != null ? "redirect:/dashboard" : "index";
  }

  /**
   * Shows the dashboard page with transactions and financial summary.
   *
   * @param principal the authenticated user
   * @param model the model to add attributes to
   * @return the view name
   */
  @GetMapping("/dashboard")
  public String showDashboard(Principal principal, Model model) {
    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new IllegalStateException("User not found"));

    List<Transaction> transactions = transactionUseCase.getUserTransactions(user);
    List<TransactionDto> transactionDtos = transactions.stream()
        .map(TransactionDto::fromDomain)
        .collect(Collectors.toList());

    FinancialSummary summary = transactionUseCase.getFinancialSummary(user);

    model.addAttribute("transactions", transactionDtos);
    model.addAttribute("totalIncome", summary.totalIncome());
    model.addAttribute("totalExpenses", summary.totalExpenses());
    model.addAttribute("balance", summary.balance());
    model.addAttribute("newTransaction", new TransactionDto());
    model.addAttribute("transactionTypes", TransactionType.values());

    return "dashboard";
  }

  /**
   * Shows the add transaction form.
   *
   * @param model the model to add attributes to
   * @return the view name
   */
  @GetMapping("/dashboard/add")
  public String showAddTransactionForm(Model model) {
    model.addAttribute("transaction", new TransactionDto());
    model.addAttribute("transactionTypes", TransactionType.values());
    return "transaction/add";
  }

  /**
   * Adds a new transaction.
   *
   * @param authentication the authenticated user
   * @param transactionDto the transaction data
   * @param redirectAttributes for flash messages
   * @return redirect to dashboard
   */
  @PostMapping("/dashboard/add")
  public String addTransaction(Authentication authentication,
      @ModelAttribute("transaction") TransactionDto transactionDto,
      RedirectAttributes redirectAttributes) {

    User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new IllegalStateException("User not found"));

    try {
      Transaction transaction = transactionDto.toDomain(user);
      transactionUseCase.addTransaction(user, transaction);
      redirectAttributes.addFlashAttribute("success", "Transaction added successfully");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/dashboard/add";
    }

    return "redirect:/dashboard";
  }

  /**
   * Adds a new transaction from the API.
   *
   * @param authentication the authenticated user
   * @param transactionDto the transaction data
   * @return the created transaction
   */
  public Transaction addTransactionFromApi(Authentication authentication, 
                                           TransactionDto transactionDto) {
    User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new IllegalStateException("User not found"));
    Transaction transaction = transactionDto.toDomain(user);
    return transactionUseCase.addTransaction(user, transaction);
  }
}
