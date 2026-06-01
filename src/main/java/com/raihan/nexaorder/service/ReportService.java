package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.ReportSummaryDTO;

public interface ReportService {

    ReportSummaryDTO getReportSummary();

    String exportReportCsv();
}
