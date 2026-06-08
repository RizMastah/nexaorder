package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final SpringDataNotificationRepository delegate;

    public NotificationRepositoryImpl(SpringDataNotificationRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Notification save(Notification notification) {
        return delegate.save(notification);
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return delegate.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId) {
        return delegate.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> findTop5ByUserIdOrderByCreatedAtDesc(Long userId) {
        return delegate.findTop5ByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public long countByUserIdAndIsReadFalse(Long userId) {
        return delegate.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public Optional<Notification> findByIdAndUserId(Long id, Long userId) {
        return delegate.findByIdAndUserId(id, userId);
    }
}
