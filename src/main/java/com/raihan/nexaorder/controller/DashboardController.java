package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.OrderResponseDTO;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.service.OrderService;
import com.raihan.nexaorder.service.ProductService;
import com.raihan.nexaorder.service.ReportService;
import com.raihan.nexaorder.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ReportService reportService;
    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("report", reportService.getReportSummary());
        model.addAttribute("lowStockProducts", productService.getLowStockProducts());
        return "admin/dashboard";
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard(Model model, HttpSession session) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(SessionUtil.requireUserId(session));
        long activeOrdersCount = orders.stream()
                .filter(order -> !OrderStatus.COMPLETED.equals(order.getStatus())
                        && !OrderStatus.CANCELLED.equals(order.getStatus()))
                .count();
        long completedOrdersCount = orders.stream()
                .filter(order -> OrderStatus.COMPLETED.equals(order.getStatus()))
                .count();

        model.addAttribute("activeOrdersCount", activeOrdersCount);
        model.addAttribute("completedOrdersCount", completedOrdersCount);
        model.addAttribute("recentOrders", orders.stream().limit(5).toList());
        return "customer/dashboard";
    }
}
