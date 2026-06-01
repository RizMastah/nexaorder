package com.raihan.nexaorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportSummaryDTO {

    private long totalOrders;
    private BigDecimal totalSales;
    private long pendingOrdersCount;
    private long paidOrdersCount;
    private long processingOrdersCount;
    private long completedOrdersCount;
    private long cancelledOrdersCount;
    private long lowStockProductsCount;
    private List<BestSellingProductDTO> bestSellingProducts;
    private List<OrderResponseDTO> recentOrders;
    private List<String> orderStatusLabels;
    private List<Long> orderStatusCounts;
    private List<String> bestSellingProductLabels;
    private List<Long> bestSellingProductQuantities;
    private List<String> salesSummaryLabels;
    private List<BigDecimal> salesSummaryValues;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestSellingProductDTO {

        private Long productId;
        private String productName;
        private Long quantitySold;
        private BigDecimal totalSales;
    }
}
