package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.service.NotificationService;
import com.raihan.nexaorder.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/admin/notifications")
    public String adminNotifications(@RequestParam(defaultValue = "all") String filter, Model model, HttpSession session) {
        Long userId = SessionUtil.requireUserId(session);
        model.addAttribute("notifications", notificationsForFilter(userId, filter));
        model.addAttribute("selectedFilter", normalizeFilter(filter));
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
    public String customerNotifications(@RequestParam(defaultValue = "all") String filter, Model model, HttpSession session) {
        Long userId = SessionUtil.requireUserId(session);
        model.addAttribute("notifications", notificationsForFilter(userId, filter));
        model.addAttribute("selectedFilter", normalizeFilter(filter));
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
            notificationService.markAsRead(id, SessionUtil.requireUserId(session));
            redirectAttributes.addFlashAttribute("successMessage", "Notification marked as read.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
    }

    private Object notificationsForFilter(Long userId, String filter) {
        if ("unread".equals(normalizeFilter(filter))) {
            return notificationService.getUnreadNotifications(userId);
        }

        return notificationService.getNotifications(userId);
    }

    private String normalizeFilter(String filter) {
        return "unread".equalsIgnoreCase(filter) ? "unread" : "all";
    }
}
