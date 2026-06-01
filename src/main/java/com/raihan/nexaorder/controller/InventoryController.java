package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.InventoryAdjustmentDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;
import com.raihan.nexaorder.service.InventoryService;
import com.raihan.nexaorder.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @GetMapping("/admin/inventory")
    public String inventory(Model model) {
        List<ProductResponseDTO> products = productService.getAllProducts();
        List<ProductResponseDTO> lowStockProducts = productService.getLowStockProducts();
        int totalStock = products.stream()
                .map(ProductResponseDTO::getStock)
                .filter(stock -> stock != null)
                .mapToInt(Integer::intValue)
                .sum();
        long outOfStockCount = products.stream()
                .filter(product -> Boolean.TRUE.equals(product.getOutOfStock()))
                .count();

        model.addAttribute("products", products);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("inventoryLogs", inventoryService.getInventoryLogs());
        model.addAttribute("adjustmentRequest", new InventoryAdjustmentDTO());
        model.addAttribute("totalStock", totalStock);
        model.addAttribute("outOfStockCount", outOfStockCount);
        model.addAttribute("lowStockThreshold", ProductService.LOW_STOCK_THRESHOLD);
        return "admin/inventory";
    }

    @PostMapping("/admin/inventory/adjust/{productId}")
    public String adjustStock(@PathVariable Long productId,
                              @Valid @ModelAttribute("adjustmentRequest") InventoryAdjustmentDTO request,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", firstValidationMessage(bindingResult));
            return "redirect:/admin/inventory";
        }

        try {
            inventoryService.adjustProductStock(productId, request);
            redirectAttributes.addFlashAttribute("successMessage", "Stock has been adjusted.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/admin/inventory";
    }

    private String firstValidationMessage(BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            return fieldError.getDefaultMessage();
        }

        return "Inventory adjustment is invalid.";
    }
}
