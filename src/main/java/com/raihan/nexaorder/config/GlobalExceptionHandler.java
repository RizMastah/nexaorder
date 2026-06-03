package com.raihan.nexaorder.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBusinessError(IllegalArgumentException exception,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", friendlyMessage(exception));

        String path = request.getRequestURI();
        if (path.startsWith("/admin/products")) {
            return "redirect:/admin/products";
        }
        if (path.startsWith("/customer/products")) {
            return "redirect:/customer/products";
        }
        if (path.startsWith("/admin/orders")) {
            return "redirect:/admin/orders";
        }
        if (path.startsWith("/customer/orders")) {
            return "redirect:/customer/orders";
        }
        if (path.startsWith("/admin/payments")) {
            return "redirect:/admin/payments";
        }
        if (path.startsWith("/customer/payment")) {
            return "redirect:/customer/orders";
        }
        if (path.startsWith("/admin/inventory")) {
            return "redirect:/admin/inventory";
        }
        if (path.startsWith("/admin/reports")) {
            return "redirect:/admin/reports";
        }
        if (path.startsWith("/admin/invoices")) {
            return "redirect:/admin/orders";
        }
        if (path.startsWith("/customer/invoices")) {
            return "redirect:/customer/orders";
        }
        if (path.startsWith("/admin/notifications")) {
            return "redirect:/admin/notifications";
        }
        if (path.startsWith("/customer/notifications")) {
            return "redirect:/customer/notifications";
        }
        if (path.startsWith("/admin")) {
            return "redirect:/admin/dashboard";
        }
        if (path.startsWith("/customer")) {
            return "redirect:/customer/dashboard";
        }

        return "redirect:/auth/login";
    }

    private String friendlyMessage(IllegalArgumentException exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return "The requested action could not be completed.";
        }

        return message;
    }
}
