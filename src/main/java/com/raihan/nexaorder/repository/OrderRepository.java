package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @Query("select distinct o from CustomerOrder o where o.user.id = :userId order by o.createdAt desc")
    List<Order> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @Query("select o from CustomerOrder o where o.id = :id and o.user.id = :userId")
    Optional<Order> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @Query("select distinct o from CustomerOrder o order by o.createdAt desc")
    List<Order> findAllDetailedByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.product"})
    @Query("select o from CustomerOrder o where o.id = :id")
    Optional<Order> findDetailedById(@Param("id") Long id);
}
