package com.raihan.nexaorder.dto;

import com.raihan.nexaorder.entity.Order;
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
public class PaymentResponseDTO {

    private Long id;
    private Long orderId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private Boolean confirmable;
    private Boolean payable;
    private List<OrderResponseDTO.OrderItemDTO> items;

    public static PaymentResponseDTO fromEntity(Payment payment) {
        Order order = payment.getOrder();

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .customerName(order.getUser().getName())
                .customerEmail(order.getUser().getEmail())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .orderStatus(order.getStatus())
                .confirmable(PaymentMethod.BANK_TRANSFER.equals(payment.getPaymentMethod())
                        && PaymentStatus.WAITING_CONFIRMATION.equals(payment.getPaymentStatus()))
                .payable(OrderStatus.PENDING.equals(order.getStatus()))
                .items(order.getOrderItems().stream()
                        .map(OrderResponseDTO.OrderItemDTO::fromEntity)
                        .toList())
                .build();
    }

    public static PaymentResponseDTO fromOrder(Order order) {
        return PaymentResponseDTO.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .customerName(order.getUser().getName())
                .customerEmail(order.getUser().getEmail())
                .amount(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .payable(OrderStatus.PENDING.equals(order.getStatus()))
                .confirmable(false)
                .items(order.getOrderItems().stream()
                        .map(OrderResponseDTO.OrderItemDTO::fromEntity)
                .toList())
                .build();
    }

    public String getDisplayCustomerName() {
        if (customerName == null || customerName.isBlank()) {
            return "Customer";
        }

        if ("NexaOrder Customer".equalsIgnoreCase(customerName.trim())) {
            return "Ariel Hikmat";
        }

        return customerName;
    }
}
