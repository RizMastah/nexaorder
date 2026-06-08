package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;

public class CompletedState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.COMPLETED;
    }

    @Override
    public OrderStatus pay() {
        throw invalid(OrderStatus.PAID);
    }

    @Override
    public OrderStatus process() {
        throw invalid(OrderStatus.PROCESSING);
    }

    @Override
    public OrderStatus complete() {
        throw invalid(OrderStatus.COMPLETED);
    }

    @Override
    public OrderStatus cancel() {
        throw invalid(OrderStatus.CANCELLED);
    }
}
