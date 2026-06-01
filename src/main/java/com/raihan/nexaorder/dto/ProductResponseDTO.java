package com.raihan.nexaorder.dto;

import com.raihan.nexaorder.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean lowStock;
    private Boolean outOfStock;

    public static ProductResponseDTO fromEntity(Product product, int lowStockThreshold) {
        int currentStock = product.getStock() == null ? 0 : product.getStock();

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(currentStock)
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .lowStock(currentStock > 0 && currentStock <= lowStockThreshold)
                .outOfStock(currentStock <= 0)
                .build();
    }
}
