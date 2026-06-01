package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.PaymentRequestDTO;
import com.raihan.nexaorder.dto.PaymentResponseDTO;
import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.entity.OrderItem;
import com.raihan.nexaorder.entity.Payment;
import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.enums.InventoryChangeType;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.enums.PaymentMethod;
import com.raihan.nexaorder.enums.PaymentStatus;
import com.raihan.nexaorder.pattern.payment.PaymentFactory;
import com.raihan.nexaorder.pattern.payment.PaymentResult;
import com.raihan.nexaorder.pattern.payment.PaymentStrategy;
import com.raihan.nexaorder.repository.OrderRepository;
import com.raihan.nexaorder.repository.PaymentRepository;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.service.InventoryService;
import com.raihan.nexaorder.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final PaymentFactory paymentFactory;

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentPageData(Long orderId, Long userId) {
        return paymentRepository.findByOrderIdAndUserId(orderId, userId)
                .map(PaymentResponseDTO::fromEntity)
                .orElseGet(() -> {
                    Order order = findOrderForCustomer(orderId, userId);
                    validateOrderCanBePaid(order);
                    return PaymentResponseDTO.fromOrder(order);
                });
    }

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(Long orderId, Long userId, PaymentRequestDTO request) {
        Order order = findOrderForCustomer(orderId, userId);
        validateOrderCanBePaid(order);

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalArgumentException("Payment has already been selected for this order");
        }

        PaymentStrategy strategy = paymentFactory.getStrategy(request.getPaymentMethod());
        PaymentResult result = strategy.process(order, order.getTotalPrice());

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(strategy.getPaymentMethod())
                .paymentStatus(result.paymentStatus())
                .amount(order.getTotalPrice())
                .paidAt(result.paidAt())
                .build();

        applyPaymentResult(order, result, "Payment " + strategy.getPaymentMethod() + " for order #" + order.getId());
        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResponseDTO.fromEntity(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponseDTO confirmBankTransferPayment(Long paymentId) {
        Payment payment = paymentRepository.findDetailedById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment was not found"));

        if (!PaymentMethod.BANK_TRANSFER.equals(payment.getPaymentMethod())) {
            throw new IllegalArgumentException("Only bank transfer payments can be confirmed");
        }

        if (!PaymentStatus.WAITING_CONFIRMATION.equals(payment.getPaymentStatus())) {
            throw new IllegalArgumentException("Only waiting bank transfer payments can be confirmed");
        }

        Order order = payment.getOrder();
        if (OrderStatus.CANCELLED.equals(order.getStatus()) || OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new IllegalArgumentException("This order cannot be confirmed");
        }

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        applyPaymentResult(
                order,
                new PaymentResult(PaymentStatus.PAID, OrderStatus.PAID, payment.getPaidAt()),
                "Bank transfer confirmed for order #" + order.getId()
        );

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponseDTO.fromEntity(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAllDetailedByOrderByCreatedAtDesc()
                .stream()
                .map(PaymentResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentResponseDTO> findPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(PaymentResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByOrderId(Long orderId) {
        return findPaymentByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Payment was not found"));
    }

    private Order findOrderForCustomer(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found"));
    }

    private void validateOrderCanBePaid(Order order) {
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new IllegalArgumentException("Cancelled orders cannot be paid");
        }

        if (OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new IllegalArgumentException("Completed orders cannot be paid");
        }

        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new IllegalArgumentException("This order has already moved past payment selection");
        }
    }

    private void applyPaymentResult(Order order, PaymentResult result, String inventoryDescription) {
        if (!OrderStatus.PAID.equals(result.orderStatus())) {
            return;
        }

        if (OrderStatus.PAID.equals(order.getStatus())) {
            return;
        }

        validateStockForPaidOrder(order);
        reduceStockForPaidOrder(order, inventoryDescription);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    private void validateStockForPaidOrder(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int currentStock = product.getStock() == null ? 0 : product.getStock();

            if (currentStock < item.getQuantity()) {
                throw new IllegalArgumentException("Stock is no longer sufficient for " + product.getName());
            }
        }
    }

    private void reduceStockForPaidOrder(Order order, String inventoryDescription) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int currentStock = product.getStock() == null ? 0 : product.getStock();
            int quantity = item.getQuantity();

            product.setStock(currentStock - quantity);
            productRepository.save(product);
            inventoryService.createInventoryLog(
                    product,
                    InventoryChangeType.OUT,
                    -quantity,
                    inventoryDescription
            );
        }
    }
}
