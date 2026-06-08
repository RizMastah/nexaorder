package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Invoice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataInvoiceRepository extends JpaRepository<Invoice, Long> {

    @EntityGraph(attributePaths = {"order", "order.user", "order.payment", "order.orderItems", "order.orderItems.product"})
    Optional<Invoice> findByOrderId(Long orderId);

    @EntityGraph(attributePaths = {"order", "order.user", "order.payment", "order.orderItems", "order.orderItems.product"})
    @Query("select i from Invoice i where i.id = :id")
    Optional<Invoice> findDetailedById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"order", "order.user", "order.payment", "order.orderItems", "order.orderItems.product"})
    @Query("select i from Invoice i where i.order.id = :orderId and i.order.user.id = :userId")
    Optional<Invoice> findByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
}

