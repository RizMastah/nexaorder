package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.PaymentRequestDTO;
import com.raihan.nexaorder.enums.PaymentMethod;
import com.raihan.nexaorder.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/customer/payment/{orderId}")
    public String paymentPage(@PathVariable Long orderId,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            preparePaymentPage(model, orderId, currentUserId(session), new PaymentRequestDTO(PaymentMethod.EWALLET));
            return "customer/payment";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/customer/orders/" + orderId;
        }
    }

    @PostMapping("/customer/payment/{orderId}")
    public String processPayment(@PathVariable Long orderId,
                                 @Valid @ModelAttribute("paymentRequest") PaymentRequestDTO request,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long userId = currentUserId(session);

        if (bindingResult.hasErrors()) {
            preparePaymentPage(model, orderId, userId, request);
            return "customer/payment";
        }

        try {
            paymentService.processPayment(orderId, userId, request);
            redirectAttributes.addFlashAttribute("successMessage", "Payment option has been submitted.");
            return "redirect:/customer/orders/" + orderId;
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            preparePaymentPage(model, orderId, userId, request);
            return "customer/payment";
        }
    }

    @GetMapping("/admin/payments")
    public String adminPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "admin/payments";
    }

    @PostMapping("/admin/payments/{paymentId}/confirm")
    public String confirmBankTransfer(@PathVariable Long paymentId, RedirectAttributes redirectAttributes) {
        try {
            paymentService.confirmBankTransferPayment(paymentId);
            redirectAttributes.addFlashAttribute("successMessage", "Bank transfer payment has been confirmed.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/admin/payments";
    }

    private void preparePaymentPage(Model model, Long orderId, Long userId, PaymentRequestDTO request) {
        model.addAttribute("paymentData", paymentService.getPaymentPageData(orderId, userId));
        model.addAttribute("paymentRequest", request);
        model.addAttribute("paymentMethods", PaymentMethod.values());
    }

    private Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Number number) {
            return number.longValue();
        }

        throw new IllegalArgumentException("User session is invalid");
    }
}
