package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.service.InvoiceService;
import com.raihan.nexaorder.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/customer/invoices/{orderId}")
    public String customerInvoice(@PathVariable Long orderId,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("invoice", invoiceService.getInvoiceByOrderIdForUser(orderId, SessionUtil.requireUserId(session)));
            return "customer/invoice";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/customer/orders";
        }
    }

    @GetMapping("/admin/invoices/{orderId}")
    public String adminInvoice(@PathVariable Long orderId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("invoice", invoiceService.getInvoiceByOrderId(orderId));
            return "admin/invoice";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/admin/orders";
        }
    }
}
