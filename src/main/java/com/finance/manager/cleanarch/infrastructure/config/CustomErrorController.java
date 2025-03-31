package com.finance.manager.cleanarch.infrastructure.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Custom error controller to handle all errors in a user-friendly way.
 * Provides custom error pages for common HTTP errors and a generic error page for others.
 */
@Controller
public class CustomErrorController implements ErrorController {

  /**
   * Handles all errors and directs to appropriate error pages.
   *
   * @param request the HTTP request
   * @param model the model to add attributes to
   * @return the name of the error view to render
   */
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    // Clear any existing error messages that might be in the model
    model.asMap().clear();
    
    // Add common error attributes to model
    model.addAttribute("timestamp", new Date());
    
    Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
    model.addAttribute("path", path != null ? path : request.getRequestURI());
    
    // Handle specific error codes
    final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());
      model.addAttribute("status", statusCode);
      
      // Add HTTP status description
      HttpStatus httpStatus = HttpStatus.resolve(statusCode);
      if (httpStatus != null) {
        model.addAttribute("error", httpStatus.getReasonPhrase());
      }
      
      // Add custom message based on error type
      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        model.addAttribute("message", "We couldn't find the page you were looking for. "
            + "The page may have been moved, deleted, or never existed in the first place.");
        return "error/404";
      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
        model.addAttribute("message", "You don't have permission to access this page. "
            + "This area might be restricted to certain users or require you to log in first.");
        return "error/403";
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        model.addAttribute("message", "Something went wrong on our end. "
            + "Our team has been notified and is working to fix the issue. "
            + "Please try again later or contact support if the problem persists.");
        return "error/500";
      }
    }
    
    // Add exception details in development mode
    final Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    boolean includeException = Boolean.TRUE.equals(
        request.getAttribute(
            "org.springframework.boot.web.servlet.error.DefaultErrorAttributes.includeException"));
    
    if (exception != null && includeException) {
      Throwable throwable = (Throwable) exception;
      model.addAttribute("exception", throwable.getClass().getName());
      model.addAttribute("trace", throwable.getStackTrace());
    }
    
    // Default to generic error page with a generic message
    model.addAttribute("message", "We're sorry, but something went wrong. "
        + "Our team has been notified and is working to fix the issue.");
    return "error";
  }
}
