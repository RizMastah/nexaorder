package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.ProductRequestDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;
import com.raihan.nexaorder.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    @GetMapping("/admin/products")
    public String adminProducts(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String category,
                                Model model) {
        model.addAttribute("products", productService.searchProducts(keyword, category));
        model.addAttribute("categories", productService.getCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("lowStockThreshold", ProductService.LOW_STOCK_THRESHOLD);
        return "admin/products";
    }

    @GetMapping("/admin/products/create")
    public String createProductForm(Model model) {
        model.addAttribute("productRequest", new ProductRequestDTO());
        prepareProductForm(model, "Create Product", "Add product", "/admin/products", false, null);
        return "admin/product-form";
    }

    @PostMapping("/admin/products")
    public String createProduct(@Valid @ModelAttribute("productRequest") ProductRequestDTO request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepareProductForm(model, "Create Product", "Add product", "/admin/products", false, null);
            return "admin/product-form";
        }

        try {
            productService.createProduct(request);
            redirectAttributes.addFlashAttribute("successMessage", "Product has been created.");
            return "redirect:/admin/products";
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("productSaveError", exception.getMessage());
            prepareProductForm(model, "Create Product", "Add product", "/admin/products", false, null);
            return "admin/product-form";
        }
    }

    @GetMapping("/admin/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        ProductResponseDTO product = productService.getProductById(id);
        model.addAttribute("productRequest", ProductRequestDTO.fromResponse(product));
        prepareProductForm(model, "Edit Product", "Update product", "/admin/products/update/" + id, true, product);
        return "admin/product-form";
    }

    @PostMapping("/admin/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("productRequest") ProductRequestDTO request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        ProductResponseDTO existingProduct = productService.getProductById(id);

        if (bindingResult.hasErrors()) {
            prepareProductForm(model, "Edit Product", "Update product", "/admin/products/update/" + id, true, existingProduct);
            return "admin/product-form";
        }

        try {
            productService.updateProduct(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Product has been updated.");
            return "redirect:/admin/products";
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("productSaveError", exception.getMessage());
            prepareProductForm(model, "Edit Product", "Update product", "/admin/products/update/" + id, true, existingProduct);
            return "admin/product-form";
        }
    }

    @PostMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product has been deleted.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product could not be deleted. Check whether it is already used in orders.");
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/customer/products")
    public String customerProducts(@RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String category,
                                   Model model) {
        model.addAttribute("products", productService.searchProducts(keyword, category));
        model.addAttribute("categories", productService.getCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        return "customer/products";
    }

    @GetMapping("/customer/products/{id}")
    public String customerProductDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "customer/product-detail";
    }

    private void prepareProductForm(Model model,
                                    String pageTitle,
                                    String submitText,
                                    String formAction,
                                    boolean editMode,
                                    ProductResponseDTO existingProduct) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("submitText", submitText);
        model.addAttribute("formAction", formAction);
        model.addAttribute("editMode", editMode);
        model.addAttribute("existingProduct", existingProduct);
        model.addAttribute("categories", productService.getCategories());
    }
}
