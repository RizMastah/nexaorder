package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;

import java.util.List;

public class ProcessingState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PROCESSING;
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
        return OrderStatus.COMPLETED;
    }

    @Override
    public OrderStatus cancel() {
        throw invalid(OrderStatus.CANCELLED);
    }

    @Override
    public List<OrderStatus> availableTransitions() {
        return List.of(OrderStatus.COMPLETED);
    }
}
