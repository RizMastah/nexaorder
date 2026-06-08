package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final SpringDataPaymentRepository delegate;

    public PaymentRepositoryImpl(SpringDataPaymentRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Payment save(Payment payment) {
        return delegate.save(payment);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return delegate.findByOrderId(orderId);
    }

    @Override
    public Optional<Payment> findDetailedById(Long id) {
        return delegate.findDetailedById(id);
    }

    @Override
    public Optional<Payment> findByOrderIdAndUserId(Long orderId, Long userId) {
        return delegate.findByOrderIdAndUserId(orderId, userId);
    }

    @Override
    public List<Payment> findAllDetailedByOrderByCreatedAtDesc() {
        return delegate.findAllDetailedByOrderByCreatedAtDesc();
    }
}
