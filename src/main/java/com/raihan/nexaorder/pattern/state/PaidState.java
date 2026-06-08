package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;

import java.util.List;

public class PaidState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PAID;
    }

    @Override
    public OrderStatus pay() {
        throw invalid(OrderStatus.PAID);
    }

    @Override
    public OrderStatus process() {
        return OrderStatus.PROCESSING;
    }

    @Override
    public OrderStatus complete() {
        throw invalid(OrderStatus.COMPLETED);
    }

    @Override
    public OrderStatus cancel() {
        throw invalid(OrderStatus.CANCELLED);
    }

    @Override
    public List<OrderStatus> availableTransitions() {
        return List.of(OrderStatus.PROCESSING);
    }
}
