package com.raihan.nexaorder.pattern.state;

import com.raihan.nexaorder.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderStateFactory {

    private final List<OrderState> states;

    public OrderState getState(OrderStatus status) {
        return states.stream()
                .filter(state -> state.getStatus().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported order status"));
    }
}
