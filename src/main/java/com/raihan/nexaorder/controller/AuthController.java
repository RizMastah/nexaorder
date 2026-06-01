package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.LoginRequestDTO;
import com.raihan.nexaorder.dto.RegisterRequestDTO;
import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;
import com.raihan.nexaorder.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login")
    public String showLoginForm(Model model, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return redirectByRole((String) session.getAttribute("userRole"));
        }

        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "auth/login";
    }

    @PostMapping("/auth/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequestDTO request,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            User user = authService.login(request);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole().name());

            return redirectByRole(user.getRole().name());
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/auth/register")
    public String showRegisterForm(Model model, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return redirectByRole((String) session.getAttribute("userRole"));
        }

        model.addAttribute("registerRequest", new RegisterRequestDTO());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequestDTO request,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.registerCustomer(request);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please sign in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/auth/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been signed out.");
        return "redirect:/auth/login";
    }

    private String redirectByRole(String role) {
        if (UserRole.ADMIN.name().equals(role)) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/customer/dashboard";
    }
}
