package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.PaymentMethod;
import com.raihan.nexaorder.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class EWalletPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.EWALLET;
    }

    @Override
    public PaymentResult process(Order order, BigDecimal amount) {
        return new PaymentResult(PaymentStatus.PAID, OrderStatus.PAID, LocalDateTime.now());
    }
}
