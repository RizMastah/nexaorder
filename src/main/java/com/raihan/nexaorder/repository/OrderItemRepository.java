package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = "product")
    List<OrderItem> findByOrderId(Long orderId);
}
