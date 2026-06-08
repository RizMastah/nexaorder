package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findAllDetailedByOrderByCreatedAtDesc();

    Optional<Order> findDetailedById(Long id);
}
