package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {

    List<NotificationResponseDTO> getNotifications(Long userId);

    List<NotificationResponseDTO> getUnreadNotifications(Long userId);

    List<NotificationResponseDTO> getRecentNotifications(Long userId);

    long countUnreadNotifications(Long userId);

    void markAsRead(Long notificationId, Long userId);
}
