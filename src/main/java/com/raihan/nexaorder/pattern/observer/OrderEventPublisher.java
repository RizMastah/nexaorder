package com.raihan.nexaorder.pattern.observer;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderEventPublisher {

    private final List<OrderEventObserver> observers = new ArrayList<>();

    public void registerObserver(OrderEventObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregisterObserver(OrderEventObserver observer) {
        observers.remove(observer);
    }

    public void publishStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        List.copyOf(observers)
                .forEach(observer -> observer.onOrderStatusChanged(order, oldStatus, newStatus));
    }
}
