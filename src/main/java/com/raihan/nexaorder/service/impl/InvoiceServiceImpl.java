package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.InvoiceResponseDTO;
import com.raihan.nexaorder.entity.Invoice;
import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.repository.InvoiceRepository;
import com.raihan.nexaorder.repository.OrderRepository;
import com.raihan.nexaorder.service.InvoiceService;
import com.raihan.nexaorder.util.InvoiceNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final InvoiceNumberGenerator invoiceNumberGenerator = InvoiceNumberGenerator.getInstance();

    @Override
    @Transactional
    public InvoiceResponseDTO generateInvoiceForOrder(Long orderId) {
        return invoiceRepository.findByOrderId(orderId)
                .map(InvoiceResponseDTO::fromEntity)
                .orElseGet(() -> createInvoice(orderId));
    }

    @Override
    @Transactional
    public InvoiceResponseDTO getInvoiceByOrderId(Long orderId) {
        return generateInvoiceForOrder(orderId);
    }

    @Override
    @Transactional
    public InvoiceResponseDTO getInvoiceByOrderIdForUser(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found"));

        return generateInvoiceForOrder(order.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDTO getInvoiceById(Long invoiceId) {
        return invoiceRepository.findDetailedById(invoiceId)
                .map(InvoiceResponseDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Invoice was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceResponseDTO> findInvoiceByOrderId(Long orderId) {
        return invoiceRepository.findByOrderId(orderId)
                .map(InvoiceResponseDTO::fromEntity);
    }

    private InvoiceResponseDTO createInvoice(Long orderId) {
        Order order = orderRepository.findDetailedById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found"));
        if (!canCreateInvoice(order.getStatus())) {
            throw new IllegalArgumentException("Invoice is only available after the order is paid");
        }

        LocalDateTime issuedAt = LocalDateTime.now();

        Invoice invoice = Invoice.builder()
                .order(order)
                .invoiceNumber(nextInvoiceNumber(issuedAt))
                .issuedAt(issuedAt)
                .totalAmount(order.getTotalPrice())
                .build();

        return InvoiceResponseDTO.fromEntity(invoiceRepository.save(invoice));
    }

    private boolean canCreateInvoice(OrderStatus status) {
        return OrderStatus.PAID.equals(status)
                || OrderStatus.PROCESSING.equals(status)
                || OrderStatus.COMPLETED.equals(status);
    }

    private String nextInvoiceNumber(LocalDateTime issuedAt) {
        long nextNumber = invoiceRepository.count() + 1;
        return invoiceNumberGenerator.generate(nextNumber, issuedAt);
    }
}
