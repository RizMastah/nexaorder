package com.raihan.nexaorder.pattern.observer;

import com.raihan.nexaorder.entity.OrderItem;
import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.enums.InventoryChangeType;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@RequiredArgsConstructor
public class InventoryObserver implements OrderEventObserver {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Override
    public void onOrderStatusChanged(com.raihan.nexaorder.entity.Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (!OrderStatus.PAID.equals(newStatus) || OrderStatus.PAID.equals(oldStatus)) {
            return;
        }

        validateStock(order);
        reduceStock(order);
    }

    private void validateStock(com.raihan.nexaorder.entity.Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int currentStock = product.getStock() == null ? 0 : product.getStock();

            if (currentStock < item.getQuantity()) {
                throw new IllegalArgumentException("Stock is no longer sufficient for " + product.getName());
            }
        }
    }

    private void reduceStock(com.raihan.nexaorder.entity.Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            int currentStock = product.getStock() == null ? 0 : product.getStock();

            product.setStock(currentStock - quantity);
            productRepository.save(product);
            inventoryService.createInventoryLog(
                    product,
                    InventoryChangeType.OUT,
                    -quantity,
                    "Stock reduced for order #" + order.getId()
            );
        }
    }
}
