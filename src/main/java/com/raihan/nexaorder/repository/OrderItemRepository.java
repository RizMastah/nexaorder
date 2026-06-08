package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository {

    OrderItem save(OrderItem orderItem);

    List<OrderItem> findByOrderId(Long orderId);

    boolean existsByProductId(Long productId);
}
