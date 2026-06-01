package com.raihan.nexaorder.service;

import com.raihan.nexaorder.dto.ProductRequestDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    int LOW_STOCK_THRESHOLD = 5;

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO createProduct(ProductRequestDTO request);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO request);

    void deleteProduct(Long id);

    List<ProductResponseDTO> searchProducts(String keyword, String category);

    List<ProductResponseDTO> getLowStockProducts();

    List<String> getCategories();
}
