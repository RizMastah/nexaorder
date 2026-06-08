package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.InventoryLog;

import java.util.List;

public interface InventoryLogRepository {

    InventoryLog save(InventoryLog inventoryLog);

    List<InventoryLog> findAllByOrderByCreatedAtDesc();

    List<InventoryLog> findByProductIdOrderByCreatedAtDesc(Long productId);

    void deleteByProductId(Long productId);
}
