package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.OrderResponseDTO;
import com.raihan.nexaorder.dto.ReportSummaryDTO;
import com.raihan.nexaorder.entity.Order;
import com.raihan.nexaorder.entity.OrderItem;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.repository.OrderRepository;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.service.ProductService;
import com.raihan.nexaorder.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter SALES_LABEL_FORMATTER = DateTimeFormatter.ofPattern("dd MMM");

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public ReportSummaryDTO getReportSummary() {
        return getReportSummary(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportSummaryDTO getReportSummary(YearMonth month) {
        List<Order> orders = filterOrdersByMonth(orderRepository.findAllDetailedByOrderByCreatedAtDesc(), month);
        Map<OrderStatus, Long> statusCounts = countStatuses(orders);
        List<Order> revenueOrders = revenueOrders(orders);
        BigDecimal totalSales = revenueOrders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<ReportSummaryDTO.BestSellingProductDTO> bestSellingProducts = bestSellingProducts(revenueOrders);
        List<OrderResponseDTO> recentOrders = orders.stream()
                .limit(8)
                .map(OrderResponseDTO::fromEntity)
                .toList();

        return ReportSummaryDTO.builder()
                .totalOrders(orders.size())
                .totalSales(totalSales)
                .pendingOrdersCount(statusCounts.get(OrderStatus.PENDING))
                .paidOrdersCount(statusCounts.get(OrderStatus.PAID))
                .processingOrdersCount(statusCounts.get(OrderStatus.PROCESSING))
                .completedOrdersCount(statusCounts.get(OrderStatus.COMPLETED))
                .cancelledOrdersCount(statusCounts.get(OrderStatus.CANCELLED))
                .lowStockProductsCount(productRepository.countByStockLessThanEqual(ProductService.LOW_STOCK_THRESHOLD))
                .bestSellingProducts(bestSellingProducts)
                .recentOrders(recentOrders)
                .orderStatusLabels(statusLabels())
                .orderStatusCounts(statusLabels().stream().map(label -> statusCounts.get(OrderStatus.valueOf(label))).toList())
                .bestSellingProductLabels(bestSellingProducts.stream().map(ReportSummaryDTO.BestSellingProductDTO::getProductName).toList())
                .bestSellingProductQuantities(bestSellingProducts.stream().map(ReportSummaryDTO.BestSellingProductDTO::getQuantitySold).toList())
                .salesSummaryLabels(salesSummaryLabels(month))
                .salesSummaryValues(salesSummaryValues(revenueOrders, month))
                .selectedMonth(month == null ? "" : month.toString())
                .periodLabel(month == null ? "All orders" : month.format(DateTimeFormatter.ofPattern("MMMM yyyy")))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public String exportReportCsv() {
        return exportReportCsv(null);
    }

    @Override
    @Transactional(readOnly = true)
    public String exportReportCsv(YearMonth month) {
        ReportSummaryDTO report = getReportSummary(month);
        StringBuilder csv = new StringBuilder();

        csv.append("NexaOrder Report\n");
        csv.append("Period,").append(csvValue(report.getPeriodLabel())).append("\n");
        csv.append("Metric,Value\n");
        csv.append("Total Orders,").append(report.getTotalOrders()).append("\n");
        csv.append("Total Sales,").append(report.getTotalSales()).append("\n");
        csv.append("Pending Orders,").append(report.getPendingOrdersCount()).append("\n");
        csv.append("Paid Orders,").append(report.getPaidOrdersCount()).append("\n");
        csv.append("Processing Orders,").append(report.getProcessingOrdersCount()).append("\n");
        csv.append("Completed Orders,").append(report.getCompletedOrdersCount()).append("\n");
        csv.append("Cancelled Orders,").append(report.getCancelledOrdersCount()).append("\n");
        csv.append("Low Stock Products,").append(report.getLowStockProductsCount()).append("\n\n");

        csv.append("Best-Selling Products\n");
        csv.append("Product,Quantity Sold,Total Sales\n");
        for (ReportSummaryDTO.BestSellingProductDTO product : report.getBestSellingProducts()) {
            csv.append(csvValue(product.getProductName())).append(",")
                    .append(product.getQuantitySold()).append(",")
                    .append(product.getTotalSales()).append("\n");
        }

        csv.append("\nRecent Orders\n");
        csv.append("Order ID,Customer,Email,Status,Total,Created At\n");
        for (OrderResponseDTO order : report.getRecentOrders()) {
            csv.append(order.getId()).append(",")
                    .append(csvValue(order.getCustomerName())).append(",")
                    .append(csvValue(order.getCustomerEmail())).append(",")
                    .append(order.getStatus()).append(",")
                    .append(order.getTotalPrice()).append(",")
                    .append(order.getCreatedAt()).append("\n");
        }

        return csv.toString();
    }

    private List<Order> filterOrdersByMonth(List<Order> orders, YearMonth month) {
        if (month == null) {
            return orders;
        }

        return orders.stream()
                .filter(order -> order.getCreatedAt() != null && YearMonth.from(order.getCreatedAt()).equals(month))
                .toList();
    }

    private Map<OrderStatus, Long> countStatuses(List<Order> orders) {
        Map<OrderStatus, Long> counts = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status, 0L);
        }

        orders.forEach(order -> counts.compute(order.getStatus(), (status, count) -> count == null ? 1L : count + 1L));
        return counts;
    }

    private List<Order> revenueOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> OrderStatus.PAID.equals(order.getStatus())
                        || OrderStatus.PROCESSING.equals(order.getStatus())
                        || OrderStatus.COMPLETED.equals(order.getStatus()))
                .toList();
    }

    private List<ReportSummaryDTO.BestSellingProductDTO> bestSellingProducts(List<Order> orders) {
        Map<Long, ProductSalesAccumulator> salesByProduct = new LinkedHashMap<>();

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                salesByProduct.computeIfAbsent(item.getProduct().getId(), id -> new ProductSalesAccumulator(item.getProduct().getName()))
                        .add(item.getQuantity(), item.getSubtotal());
            }
        }

        return salesByProduct.entrySet()
                .stream()
                .map(entry -> ReportSummaryDTO.BestSellingProductDTO.builder()
                        .productId(entry.getKey())
                        .productName(entry.getValue().productName())
                        .quantitySold(entry.getValue().quantitySold())
                        .totalSales(entry.getValue().totalSales())
                        .build())
                .sorted(Comparator.comparing(ReportSummaryDTO.BestSellingProductDTO::getQuantitySold).reversed())
                .limit(6)
                .toList();
    }

    private List<String> statusLabels() {
        return List.of(
                OrderStatus.PENDING.name(),
                OrderStatus.PAID.name(),
                OrderStatus.PROCESSING.name(),
                OrderStatus.COMPLETED.name(),
                OrderStatus.CANCELLED.name()
        );
    }

    private List<String> salesSummaryLabels(YearMonth month) {
        if (month != null) {
            LocalDate startDate = month.atDay(1);
            return startDate.datesUntil(month.atEndOfMonth().plusDays(1))
                    .map(date -> date.format(SALES_LABEL_FORMATTER))
                    .toList();
        }

        LocalDate startDate = LocalDate.now().minusDays(6);
        return startDate.datesUntil(LocalDate.now().plusDays(1))
                .map(date -> date.format(SALES_LABEL_FORMATTER))
                .toList();
    }

    private List<BigDecimal> salesSummaryValues(List<Order> orders, YearMonth month) {
        Map<LocalDate, BigDecimal> salesByDate = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDate(),
                        Collectors.mapping(Order::getTotalPrice, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        if (month != null) {
            LocalDate startDate = month.atDay(1);
            return startDate.datesUntil(month.atEndOfMonth().plusDays(1))
                    .map(date -> salesByDate.getOrDefault(date, BigDecimal.ZERO))
                    .toList();
        }

        LocalDate startDate = LocalDate.now().minusDays(6);
        return startDate.datesUntil(LocalDate.now().plusDays(1))
                .map(date -> salesByDate.getOrDefault(date, BigDecimal.ZERO))
                .toList();
    }

    private String csvValue(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static final class ProductSalesAccumulator {
        private final String productName;
        private long quantitySold;
        private BigDecimal totalSales = BigDecimal.ZERO;

        private ProductSalesAccumulator(String productName) {
            this.productName = productName;
        }

        private void add(Integer quantity, BigDecimal subtotal) {
            quantitySold += quantity == null ? 0 : quantity;
            totalSales = totalSales.add(subtotal == null ? BigDecimal.ZERO : subtotal);
        }

        private String productName() {
            return productName;
        }

        private Long quantitySold() {
            return quantitySold;
        }

        private BigDecimal totalSales() {
            return totalSales;
        }
    }
}
