package com.raihan.nexaorder.config;

import com.raihan.nexaorder.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final NotificationService notificationService;

    @ModelAttribute("recentNotifications")
    public Object recentNotifications(HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return List.of();
        }

        return notificationService.getRecentNotifications(userId);
    }

    @ModelAttribute("unreadNotificationCount")
    public long unreadNotificationCount(HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return 0;
        }

        return notificationService.countUnreadNotifications(userId);
    }

    private Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Number number) {
            return number.longValue();
        }

        return null;
    }
}
