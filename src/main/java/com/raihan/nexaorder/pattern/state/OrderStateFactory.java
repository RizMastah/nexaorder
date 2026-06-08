package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;

public final class OrderStateFactory {

    private static final OrderStateFactory INSTANCE = new OrderStateFactory();

    private OrderStateFactory() {
    }

    public static OrderStateFactory getInstance() {
        return INSTANCE;
    }

    public OrderState getState(OrderStatus status) {
        return createState(status);
    }

    // Manual Factory Method for the State Pattern. Spring does not create these state objects.
    public OrderState createState(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status is required");
        }

        return switch (status) {
            case PENDING -> new PendingState();
            case PAID -> new PaidState();
            case PROCESSING -> new ProcessingState();
            case COMPLETED -> new CompletedState();
            case CANCELLED -> new CancelledState();
        };
    }
}
