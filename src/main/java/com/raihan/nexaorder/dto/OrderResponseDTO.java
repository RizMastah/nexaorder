package com.raihan.nexaorder.dto;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.entity.OrderItem;
import com.raihan.nexaorder.entity.Payment;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.PaymentMethod;
import com.raihan.nexaorder.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private static final String[] DEMO_CUSTOMER_NAMES = {
            "Ariel Hikmat",
            "Denis Freeman",
            "Morgan Cox",
            "Paul Rey",
            "Maja Becker",
            "Erwan Richard",
            "Clara Moreno",
            "Rania Putri"
    };

    private Long id;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean cancellable;
    private List<OrderItemDTO> items;

    public static OrderResponseDTO fromEntity(Order order) {
        Payment payment = order.getPayment();
        List<OrderItemDTO> itemResponses = order.getOrderItems()
                .stream()
                .map(OrderItemDTO::fromEntity)
                .toList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .customerName(order.getUser().getName())
                .customerEmail(order.getUser().getEmail())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(payment == null ? null : payment.getPaymentMethod())
                .paymentStatus(payment == null ? null : payment.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .cancellable(OrderStatus.PENDING.equals(order.getStatus()))
                .items(itemResponses)
                .build();
    }

    public String getDisplayCustomerName() {
        if (customerName == null || customerName.isBlank()) {
            return "Customer";
        }

        if ("NexaOrder Customer".equalsIgnoreCase(customerName.trim())) {
            int index = Math.floorMod(id == null ? 0 : id.intValue(), DEMO_CUSTOMER_NAMES.length);
            return DEMO_CUSTOMER_NAMES[index];
        }

        return customerName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO {

        private Long id;
        private Long productId;
        private String productName;
        private String productCategory;
        private String productImageUrl;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;

        public static OrderItemDTO fromEntity(OrderItem item) {
            return OrderItemDTO.builder()
                    .id(item.getId())
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .productCategory(item.getProduct().getCategory())
                    .productImageUrl(item.getProduct().getImageUrl())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .subtotal(item.getSubtotal())
                    .build();
        }
    }
}
