package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;

import java.util.List;

public interface OrderState {

    OrderStatus getStatus();

    OrderStatus pay();

    OrderStatus process();

    OrderStatus complete();

    OrderStatus cancel();

    default OrderStatus transitionTo(OrderStatus targetStatus) {
        return switch (targetStatus) {
            case PAID -> pay();
            case PROCESSING -> process();
            case COMPLETED -> complete();
            case CANCELLED -> cancel();
            case PENDING -> throw invalid(targetStatus);
        };
    }

    default List<OrderStatus> availableTransitions() {
        return List.of();
    }

    default IllegalArgumentException invalid(OrderStatus targetStatus) {
        return new IllegalArgumentException("Order status cannot move from " + getStatus() + " to " + targetStatus);
    }
}
