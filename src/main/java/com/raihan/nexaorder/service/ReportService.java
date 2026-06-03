package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.ReportSummaryDTO;

import java.time.YearMonth;

public interface ReportService {

    ReportSummaryDTO getReportSummary();

    ReportSummaryDTO getReportSummary(YearMonth month);

    String exportReportCsv();

    String exportReportCsv(YearMonth month);
}
