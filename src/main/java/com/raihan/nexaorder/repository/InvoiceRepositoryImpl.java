package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Invoice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final SpringDataInvoiceRepository delegate;

    public InvoiceRepositoryImpl(SpringDataInvoiceRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return delegate.save(invoice);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public Optional<Invoice> findByOrderId(Long orderId) {
        return delegate.findByOrderId(orderId);
    }

    @Override
    public Optional<Invoice> findDetailedById(Long id) {
        return delegate.findDetailedById(id);
    }

    @Override
    public Optional<Invoice> findByOrderIdAndUserId(Long orderId, Long userId) {
        return delegate.findByOrderIdAndUserId(orderId, userId);
    }
}
