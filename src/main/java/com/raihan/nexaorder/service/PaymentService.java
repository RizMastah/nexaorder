package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.PaymentRequestDTO;
import com.raihan.nexaorder.dto.PaymentResponseDTO;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    PaymentResponseDTO getPaymentPageData(Long orderId, Long userId);

    PaymentResponseDTO processPayment(Long orderId, Long userId, PaymentRequestDTO request);

    PaymentResponseDTO confirmBankTransferPayment(Long paymentId);

    List<PaymentResponseDTO> getAllPayments();

    Optional<PaymentResponseDTO> findPaymentByOrderId(Long orderId);

    PaymentResponseDTO getPaymentByOrderId(Long orderId);
}
