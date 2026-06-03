package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.service.ReportService;
import com.raihan.nexaorder.util.DateFilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/admin/reports")
    public String reports(@RequestParam(required = false) String month, Model model) {
        YearMonth selectedMonth = DateFilterUtil.parseYearMonth(month);
        model.addAttribute("report", reportService.getReportSummary(selectedMonth));
        model.addAttribute("selectedMonth", DateFilterUtil.toMonthInputValue(selectedMonth));
        return "admin/reports";
    }

    @GetMapping("/admin/reports/export")
    public ResponseEntity<byte[]> exportReport(@RequestParam(required = false) String month) {
        YearMonth selectedMonth = DateFilterUtil.parseYearMonth(month);
        byte[] csv = reportService.exportReportCsv(selectedMonth).getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("nexaorder-report.csv")
                        .build()
                        .toString())
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csv);
    }
}
