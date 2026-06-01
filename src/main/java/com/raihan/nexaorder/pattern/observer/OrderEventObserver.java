package com.raihan.nexaorder.pattern.observer;

import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;

public interface OrderEventObserver {

    void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}
