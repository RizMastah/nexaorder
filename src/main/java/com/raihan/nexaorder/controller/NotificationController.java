package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/admin/notifications")
    public String adminNotifications(Model model, HttpSession session) {
        model.addAttribute("notifications", notificationService.getNotifications(currentUserId(session)));
        return "admin/notifications";
    }

    @PostMapping("/admin/notifications/{id}/read")
    public String markAdminNotificationAsRead(@PathVariable Long id,
                                              HttpSession session,
                                              RedirectAttributes redirectAttributes) {
        markAsRead(id, session, redirectAttributes);
        return "redirect:/admin/notifications";
    }

    @GetMapping("/customer/notifications")
    public String customerNotifications(Model model, HttpSession session) {
        model.addAttribute("notifications", notificationService.getNotifications(currentUserId(session)));
        return "customer/notifications";
    }

    @PostMapping("/customer/notifications/{id}/read")
    public String markCustomerNotificationAsRead(@PathVariable Long id,
                                                 HttpSession session,
                                                 RedirectAttributes redirectAttributes) {
        markAsRead(id, session, redirectAttributes);
        return "redirect:/customer/notifications";
    }

    private void markAsRead(Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            notificationService.markAsRead(id, currentUserId(session));
            redirectAttributes.addFlashAttribute("successMessage", "Notification marked as read.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
    }

    private Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Number number) {
            return number.longValue();
        }

        throw new IllegalArgumentException("User session is invalid");
    }
}
