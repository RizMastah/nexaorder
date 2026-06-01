package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.InventoryAdjustmentDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;
import com.raihan.nexaorder.entity.InventoryLog;
import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.enums.InventoryChangeType;

import java.util.List;

public interface InventoryService {

    InventoryLog createInventoryLog(Product product,
                                    InventoryChangeType changeType,
                                    Integer quantityChange,
                                    String description);

    List<InventoryLog> getInventoryLogs();

    List<InventoryLog> getInventoryLogsByProduct(Long productId);

    ProductResponseDTO adjustProductStock(Long productId, InventoryAdjustmentDTO request);
}
