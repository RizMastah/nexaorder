package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final SpringDataOrderItemRepository delegate;

    public OrderItemRepositoryImpl(SpringDataOrderItemRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return delegate.save(orderItem);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return delegate.findByOrderId(orderId);
    }

    @Override
    public boolean existsByProductId(Long productId) {
        return delegate.existsByProductId(productId);
    }
}
