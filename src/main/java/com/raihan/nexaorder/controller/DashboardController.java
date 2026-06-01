package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ReportService reportService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("report", reportService.getReportSummary());
        return "admin/dashboard";
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard() {
        return "customer/dashboard";
    }
}
