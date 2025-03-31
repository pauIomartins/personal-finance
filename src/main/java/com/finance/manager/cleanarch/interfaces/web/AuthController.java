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
   * @param registered optional registered parameter indicating successful registration
   * @param model the Spring MVC model
   * @return the name of the login view template
   */
  @GetMapping("/login")
  public String login(
      @RequestParam(required = false) String error,
      @RequestParam(required = false) String logout,
      @RequestParam(required = false) String registered,
      Model model) {
    
    if (Boolean.parseBoolean(error)) {
      model.addAttribute("error", "Invalid credentials. Please check your email and password.");
    }

    if (Boolean.parseBoolean(logout)) {
      model.addAttribute("message", "You have been successfully logged out.");
    }
    
    if (Boolean.parseBoolean(registered)) {
      model.addAttribute("message", "Registration successful! Please login.");
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
    model.addAttribute("emailPattern", User.getEmailPattern());
    model.addAttribute("passwordPattern", User.getPasswordPattern());
    model.addAttribute("user", new UserDto());
    return "register";
  }

  /**
   * Handles user registration.
   *
   * @param userDto the user data transfer object containing registration information
   * @param model for adding attributes
   * @return the name of the registration view template or redirect to login page on success
   */
  @PostMapping("/register")
  public String register(
      @ModelAttribute UserDto userDto,
      Model model) {
    try {
      // Validate password confirmation
      if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
        model.addAttribute("error", "Passwords do not match");
        model.addAttribute("user", userDto);
        model.addAttribute("passwordRequirements", 
            User.getPasswordRequirements());
        model.addAttribute("emailPattern", User.getEmailPattern());
        model.addAttribute("passwordPattern", User.getPasswordPattern());
        return "register";
      }
      
      userUseCase.registerUser(userDto.getEmail(), userDto.getPassword(), userDto.getName());
      return "redirect:/login?registered=true";
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("user", userDto);
      model.addAttribute("passwordRequirements", 
          User.getPasswordRequirements());
      model.addAttribute("emailPattern", User.getEmailPattern());
      model.addAttribute("passwordPattern", User.getPasswordPattern());
      return "register";
    }
  }
}
