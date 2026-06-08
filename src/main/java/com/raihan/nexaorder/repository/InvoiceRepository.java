package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Invoice;

import java.util.Optional;

public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    long count();

    Optional<Invoice> findByOrderId(Long orderId);

    Optional<Invoice> findDetailedById(Long id);

    Optional<Invoice> findByOrderIdAndUserId(Long orderId, Long userId);
}
