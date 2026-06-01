package com.raihan.nexaorder.pattern.observer;

import com.raihan.nexaorder.entity.Notification;
import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.UserRole;
import com.raihan.nexaorder.repository.NotificationRepository;
import com.raihan.nexaorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@RequiredArgsConstructor
public class NotificationObserver implements OrderEventObserver {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void onOrderStatusChanged(com.raihan.nexaorder.entity.Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        switch (newStatus) {
            case PAID -> {
                notifyCustomer(order, "Payment confirmed", "Your order payment has been confirmed.");
                notifyAdmins("Paid order ready", "A new paid order is ready to process.");
            }
            case PROCESSING -> notifyCustomer(order, "Order processing", "Your order is now being processed.");
            case COMPLETED -> notifyCustomer(order, "Order completed", "Your order has been completed.");
            case CANCELLED -> notifyCustomer(order, "Order cancelled", "Your order has been cancelled.");
            case PENDING -> {
            }
        }
    }

    private void notifyCustomer(com.raihan.nexaorder.entity.Order order, String title, String message) {
        createNotification(order.getUser(), title, message + " Order #" + order.getId());
    }

    private void notifyAdmins(String title, String message) {
        userRepository.findByRole(UserRole.ADMIN)
                .forEach(admin -> createNotification(admin, title, message));
    }

    private void createNotification(User user, String title, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
}
