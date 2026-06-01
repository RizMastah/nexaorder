package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.CreateOrderRequestDTO;
import com.raihan.nexaorder.dto.OrderResponseDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;
import com.raihan.nexaorder.enums.OrderStatus;
import com.raihan.nexaorder.service.InvoiceService;
import com.raihan.nexaorder.service.OrderService;
import com.raihan.nexaorder.service.PaymentService;
import com.raihan.nexaorder.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;

    @GetMapping("/customer/orders/create")
    public String createOrderForm(@RequestParam Long productId, Model model, RedirectAttributes redirectAttributes) {
        try {
            CreateOrderRequestDTO request = CreateOrderRequestDTO.builder()
                    .productId(productId)
                    .quantity(1)
                    .build();
            prepareCreateOrderModel(model, productId, request);
            return "customer/create-order";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/customer/products";
        }
    }

    @PostMapping("/customer/orders")
    public String createOrder(@Valid @ModelAttribute("orderRequest") CreateOrderRequestDTO request,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            if (request.getProductId() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product is required.");
                return "redirect:/customer/products";
            }

            prepareCreateOrderModel(model, request.getProductId(), request);
            return "customer/create-order";
        }

        try {
            Long userId = currentUserId(session);
            OrderResponseDTO order = orderService.createOrder(userId, request);
            redirectAttributes.addFlashAttribute("successMessage", "Order has been created.");
            return "redirect:/customer/orders/" + order.getId();
        } catch (IllegalArgumentException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            prepareCreateOrderModel(model, request.getProductId(), request);
            return "customer/create-order";
        }
    }

    @GetMapping("/customer/orders")
    public String customerOrders(Model model, HttpSession session) {
        model.addAttribute("orders", orderService.getOrdersByUserId(currentUserId(session)));
        return "customer/orders";
    }

    @GetMapping("/customer/orders/{id}")
    public String customerOrderDetail(@PathVariable Long id,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("order", orderService.getOrderByIdForUser(id, currentUserId(session)));
            model.addAttribute("payment", paymentService.findPaymentByOrderId(id).orElse(null));
            model.addAttribute("invoice", invoiceService.findInvoiceByOrderId(id).orElse(null));
            return "customer/order-detail";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/customer/orders";
        }
    }

    @PostMapping("/customer/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, currentUserId(session));
            redirectAttributes.addFlashAttribute("successMessage", "Order has been cancelled.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/customer/orders/" + id;
    }

    @GetMapping("/admin/orders")
    public String adminOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/admin/orders/{id}")
    public String adminOrderDetail(@PathVariable Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("order", orderService.getOrderById(id));
            model.addAttribute("payment", paymentService.findPaymentByOrderId(id).orElse(null));
            model.addAttribute("invoice", invoiceService.findInvoiceByOrderId(id).orElse(null));
            return "admin/order-detail";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/admin/orders";
        }
    }

    @PostMapping("/admin/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatusByAdmin(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status has been updated.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/admin/orders/" + id;
    }

    private void prepareCreateOrderModel(Model model, Long productId, CreateOrderRequestDTO request) {
        ProductResponseDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("orderRequest", request);
    }

    private Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Number number) {
            return number.longValue();
        }

        throw new IllegalArgumentException("User session is invalid");
    }
}
