package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/admin/reports")
    public String reports(Model model) {
        model.addAttribute("report", reportService.getReportSummary());
        return "admin/reports";
    }

    @GetMapping("/admin/reports/export")
    public ResponseEntity<byte[]> exportReport() {
        byte[] csv = reportService.exportReportCsv().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("nexaorder-report.csv")
                        .build()
                        .toString())
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csv);
    }
}
