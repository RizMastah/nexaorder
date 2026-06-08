package com.raihan.nexaorder.pattern.payment;

import com.raihan.nexaorder.enums.PaymentMethod;

public final class PaymentFactory {

    private static final PaymentFactory INSTANCE = new PaymentFactory();

    private PaymentFactory() {
    }

    public static PaymentFactory getInstance() {
        return INSTANCE;
    }

    public PaymentStrategy getStrategy(PaymentMethod paymentMethod) {
        return createStrategy(paymentMethod);
    }

    // Manual Factory Method + Strategy selection. Spring is not used to assemble these strategies.
    public PaymentStrategy createStrategy(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method is required");
        }

        return switch (paymentMethod) {
            case EWALLET -> new EWalletPaymentStrategy();
            case BANK_TRANSFER -> new BankTransferPaymentStrategy();
            case CASH_ON_DELIVERY -> new CashOnDeliveryPaymentStrategy();
        };
    }
}
