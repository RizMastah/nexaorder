package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final List<PaymentStrategy> strategies;

    public PaymentStrategy getStrategy(PaymentMethod paymentMethod) {
        return strategies.stream()
                .filter(strategy -> strategy.getPaymentMethod().equals(paymentMethod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported payment method"));
    }
}
