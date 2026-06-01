package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.InvoiceResponseDTO;

import java.util.Optional;

public interface InvoiceService {

    InvoiceResponseDTO generateInvoiceForOrder(Long orderId);

    InvoiceResponseDTO getInvoiceByOrderId(Long orderId);

    InvoiceResponseDTO getInvoiceByOrderIdForUser(Long orderId, Long userId);

    InvoiceResponseDTO getInvoiceById(Long invoiceId);

    Optional<InvoiceResponseDTO> findInvoiceByOrderId(Long orderId);
}
