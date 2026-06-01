package com.raihan.nexaorder.pattern.observer;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final List<OrderEventObserver> observers;

    public void publishStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        observers.forEach(observer -> observer.onOrderStatusChanged(order, oldStatus, newStatus));
    }
}
