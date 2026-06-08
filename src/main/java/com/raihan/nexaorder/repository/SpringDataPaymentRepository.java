package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataPaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"order", "order.user", "order.orderItems", "order.orderItems.product"})
    Optional<Payment> findByOrderId(Long orderId);

    @EntityGraph(attributePaths = {"order", "order.user", "order.orderItems", "order.orderItems.product"})
    @Query("select p from Payment p where p.id = :id")
    Optional<Payment> findDetailedById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"order", "order.user", "order.orderItems", "order.orderItems.product"})
    @Query("select p from Payment p where p.order.id = :orderId and p.order.user.id = :userId")
    Optional<Payment> findByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"order", "order.user", "order.orderItems", "order.orderItems.product"})
    @Query("select p from Payment p order by p.createdAt desc")
    List<Payment> findAllDetailedByOrderByCreatedAtDesc();
}

