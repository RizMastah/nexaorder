package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResult(
        PaymentStatus paymentStatus,
        OrderStatus orderStatus,
        LocalDateTime paidAt
) {
}
