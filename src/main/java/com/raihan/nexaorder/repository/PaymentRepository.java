package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findDetailedById(Long id);

    Optional<Payment> findByOrderIdAndUserId(Long orderId, Long userId);

    List<Payment> findAllDetailedByOrderByCreatedAtDesc();
}
