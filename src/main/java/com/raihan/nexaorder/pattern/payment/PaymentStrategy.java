package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentStrategy {

    PaymentMethod getPaymentMethod();

    PaymentResult process(Order order, BigDecimal amount);
}
