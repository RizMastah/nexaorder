package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SpringDataOrderRepository delegate;

    public OrderRepositoryImpl(SpringDataOrderRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Order save(Order order) {
        return delegate.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return delegate.findByUserId(userId);
    }

    @Override
    public List<Order> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return delegate.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Optional<Order> findByIdAndUserId(Long id, Long userId) {
        return delegate.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Order> findAllDetailedByOrderByCreatedAtDesc() {
        return delegate.findAllDetailedByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<Order> findDetailedById(Long id) {
        return delegate.findDetailedById(id);
    }
}
