package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PendingState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PENDING;
    }

    @Override
    public OrderStatus pay() {
        return OrderStatus.PAID;
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
        return OrderStatus.CANCELLED;
    }

    @Override
    public List<OrderStatus> availableTransitions() {
        return List.of(OrderStatus.PAID, OrderStatus.CANCELLED);
    }
}
