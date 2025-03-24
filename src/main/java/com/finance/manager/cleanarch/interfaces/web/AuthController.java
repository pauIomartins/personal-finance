package com.finance.manager.cleanarch.interfaces.web;

import com.finance.manager.cleanarch.application.usecase.UserUseCase;
import com.finance.manager.cleanarch.domain.model.User;
import com.finance.manager.cleanarch.interfaces.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for handling authentication-related requests.
 * Acts as an interface adapter between the web layer and application layer.
 */
@Controller
public final class AuthController {

  private final UserUseCase userUseCase;

  public AuthController(UserUseCase userUseCase) {
    this.userUseCase = userUseCase;
  }

  /**
   * Displays the login page.
   *
   * @param error optional error parameter indicating login failure
   * @param logout optional logout parameter indicating successful logout
   * @param model the Spring MVC model
   * @return the name of the login view template
   */
  @GetMapping("/login")
  public String login(
      @RequestParam(required = false) String error,
      @RequestParam(required = false) String logout,
      Model model) {
    
    if (Boolean.parseBoolean(error)) {
      model.addAttribute("error", "Invalid credentials. Please check your email and password.");
    }

    if (Boolean.parseBoolean(logout)) {
      model.addAttribute("message", "You have been successfully logged out.");
    }

    return "login";
  }

  /**
   * Displays the registration page.
   *
   * @param model the Spring MVC model
   * @return the name of the registration view template
   */
  @GetMapping("/register")
  public String registerForm(Model model) {
    model.addAttribute("passwordRequirements", User.getPasswordRequirements());
    model.addAttribute("user", new UserDto());
    return "register";
  }

  /**
   * Handles user registration.
   *
   * @param userDto the user data transfer object containing registration information
   * @param redirectAttributes for adding flash messages
   * @return redirect to login page on success or back to registration on failure
   */
  @PostMapping("/register")
  public String register(
      @ModelAttribute UserDto userDto,
      RedirectAttributes redirectAttributes) {
    try {
      userUseCase.registerUser(userDto.getEmail(), userDto.getPassword(), userDto.getName());
      redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
      return "redirect:/login";
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      redirectAttributes.addFlashAttribute("passwordRequirements", User.getPasswordRequirements());
      return "redirect:/register";
    }
  }
}
