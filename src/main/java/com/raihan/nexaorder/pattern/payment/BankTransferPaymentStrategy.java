package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.PaymentMethod;
import com.raihan.nexaorder.enums.PaymentStatus;

import java.math.BigDecimal;

public class BankTransferPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.BANK_TRANSFER;
    }

    @Override
    public PaymentResult process(Order order, BigDecimal amount) {
        return new PaymentResult(PaymentStatus.WAITING_CONFIRMATION, OrderStatus.PENDING, null);
    }
}
