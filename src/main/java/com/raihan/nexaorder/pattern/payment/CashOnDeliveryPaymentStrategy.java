package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.PaymentMethod;
import com.raihan.nexaorder.enums.PaymentStatus;

import java.math.BigDecimal;

public class CashOnDeliveryPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }

    @Override
    public PaymentResult process(Order order, BigDecimal amount) {
        return new PaymentResult(PaymentStatus.PENDING, OrderStatus.PAID, null);
    }
}
