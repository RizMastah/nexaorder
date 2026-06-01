package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
