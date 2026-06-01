package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
