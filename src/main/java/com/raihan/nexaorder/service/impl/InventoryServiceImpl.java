package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.InventoryAdjustmentDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;
import com.raihan.nexaorder.entity.InventoryLog;
import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.enums.InventoryChangeType;
import com.raihan.nexaorder.repository.InventoryLogRepository;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.service.InventoryService;
import com.raihan.nexaorder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryLogRepository inventoryLogRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public InventoryLog createInventoryLog(Product product,
                                           InventoryChangeType changeType,
                                           Integer quantityChange,
                                           String description) {
        InventoryLog inventoryLog = InventoryLog.builder()
                .product(product)
                .changeType(changeType)
                .quantityChange(quantityChange)
                .description(description)
                .build();

        return inventoryLogRepository.save(inventoryLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryLog> getInventoryLogs() {
        return inventoryLogRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryLog> getInventoryLogsByProduct(Long productId) {
        return inventoryLogRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Override
    @Transactional
    public ProductResponseDTO adjustProductStock(Long productId, InventoryAdjustmentDTO request) {
        if (request.getQuantityChange() == null || request.getQuantityChange() == 0) {
            throw new IllegalArgumentException("Quantity change cannot be zero");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product was not found"));

        int currentStock = product.getStock() == null ? 0 : product.getStock();
        int updatedStock = currentStock + request.getQuantityChange();
        if (updatedStock < 0) {
            throw new IllegalArgumentException("Stock adjustment cannot make stock negative");
        }

        product.setStock(updatedStock);
        Product savedProduct = productRepository.save(product);

        String description = request.getDescription() == null || request.getDescription().trim().isEmpty()
                ? "Manual stock adjustment from " + currentStock + " to " + updatedStock
                : request.getDescription().trim();

        createInventoryLog(savedProduct, InventoryChangeType.ADJUSTMENT, request.getQuantityChange(), description);

        return ProductResponseDTO.fromEntity(savedProduct, ProductService.LOW_STOCK_THRESHOLD);
    }
}
