package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.CreateOrderRequestDTO;
import com.raihan.nexaorder.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(Long userId, CreateOrderRequestDTO request);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    OrderResponseDTO getOrderById(Long orderId);

    OrderResponseDTO getOrderByIdForUser(Long orderId, Long userId);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO cancelOrder(Long orderId, Long userId);
}
