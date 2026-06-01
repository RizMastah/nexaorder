package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.CreateOrderRequestDTO;
import com.raihan.nexaorder.dto.OrderItemRequestDTO;
import com.raihan.nexaorder.dto.OrderResponseDTO;
import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.entity.OrderItem;
import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.repository.OrderItemRepository;
import com.raihan.nexaorder.repository.OrderRepository;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.repository.UserRepository;
import com.raihan.nexaorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(Long userId, CreateOrderRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer was not found"));

        OrderItemRequestDTO itemRequest = request.toOrderItemRequest();
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product was not found"));

        int quantity = validateQuantity(itemRequest.getQuantity());
        validateStock(product, quantity);

        BigDecimal price = product.getPrice();
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalPrice(subtotal)
                .build();

        Order savedOrder = orderRepository.save(order);
        OrderItem orderItem = OrderItem.builder()
                .order(savedOrder)
                .product(product)
                .quantity(quantity)
                .price(price)
                .subtotal(subtotal)
                .build();

        OrderItem savedItem = orderItemRepository.save(orderItem);
        savedOrder.getOrderItems().add(savedItem);

        return OrderResponseDTO.fromEntity(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found"));

        return OrderResponseDTO.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderByIdForUser(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found"));

        return OrderResponseDTO.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAllDetailedByOrderByCreatedAtDesc()
                .stream()
                .map(OrderResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found"));

        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Only pending orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return OrderResponseDTO.fromEntity(savedOrder);
    }

    private int validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        return quantity;
    }

    private void validateStock(Product product, int quantity) {
        int availableStock = product.getStock() == null ? 0 : product.getStock();
        if (availableStock < quantity) {
            throw new IllegalArgumentException("Product stock is not sufficient");
        }
    }
}
