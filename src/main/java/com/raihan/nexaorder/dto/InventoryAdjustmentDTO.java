package com.raihan.nexaorder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryAdjustmentDTO {

    @NotNull(message = "Quantity change is required")
    private Integer quantityChange;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
