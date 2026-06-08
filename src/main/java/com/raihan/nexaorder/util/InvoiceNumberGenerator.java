package com.raihan.nexaorder.util;

import java.time.LocalDateTime;

public final class InvoiceNumberGenerator {

    private static final InvoiceNumberGenerator INSTANCE = new InvoiceNumberGenerator();

    private InvoiceNumberGenerator() {
    }

    public static InvoiceNumberGenerator getInstance() {
        return INSTANCE;
    }

    // Manual Singleton Pattern utility for consistent invoice numbering.
    public String generate(long sequenceNumber, LocalDateTime issuedAt) {
        if (sequenceNumber <= 0) {
            throw new IllegalArgumentException("Invoice sequence number must be greater than zero");
        }

        LocalDateTime timestamp = issuedAt == null ? LocalDateTime.now() : issuedAt;
        return "INV-" + timestamp.getYear() + "-" + String.format("%04d", sequenceNumber);
    }
}
