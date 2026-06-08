package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.InventoryLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryLogRepositoryImpl implements InventoryLogRepository {

    private final SpringDataInventoryLogRepository delegate;

    public InventoryLogRepositoryImpl(SpringDataInventoryLogRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public InventoryLog save(InventoryLog inventoryLog) {
        return delegate.save(inventoryLog);
    }

    @Override
    public List<InventoryLog> findAllByOrderByCreatedAtDesc() {
        return delegate.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<InventoryLog> findByProductIdOrderByCreatedAtDesc(Long productId) {
        return delegate.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Override
    public void deleteByProductId(Long productId) {
        delegate.deleteByProductId(productId);
    }
}
