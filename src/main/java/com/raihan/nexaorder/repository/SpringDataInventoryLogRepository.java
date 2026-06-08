package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.InventoryLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataInventoryLogRepository extends JpaRepository<InventoryLog, Long> {

    @EntityGraph(attributePaths = "product")
    List<InventoryLog> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "product")
    List<InventoryLog> findByProductIdOrderByCreatedAtDesc(Long productId);

    void deleteByProductId(Long productId);
}

