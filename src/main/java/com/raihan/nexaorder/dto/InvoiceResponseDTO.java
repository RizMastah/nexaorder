package com.raihan.nexaorder.dto;

import com.raihan.nexaorder.entity.Invoice;
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
public class InvoiceResponseDTO {

    private Long id;
    private Long orderId;
    private String invoiceNumber;
    private LocalDateTime issuedAt;
    private BigDecimal totalAmount;
    private String customerName;
    private String customerEmail;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private List<OrderResponseDTO.OrderItemDTO> items;

    public static InvoiceResponseDTO fromEntity(Invoice invoice) {
        Order order = invoice.getOrder();
        Payment payment = order.getPayment();

        return InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .orderId(order.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .issuedAt(invoice.getIssuedAt())
                .totalAmount(invoice.getTotalAmount())
                .customerName(order.getUser().getName())
                .customerEmail(order.getUser().getEmail())
                .orderDate(order.getCreatedAt())
                .orderStatus(order.getStatus())
                .paymentMethod(payment == null ? null : payment.getPaymentMethod())
                .paymentStatus(payment == null ? null : payment.getPaymentStatus())
                .items(order.getOrderItems()
                        .stream()
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
