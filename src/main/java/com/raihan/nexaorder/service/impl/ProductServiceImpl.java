package com.raihan.nexaorder.service.impl;

import com.raihan.nexaorder.dto.ProductRequestDTO;
import com.raihan.nexaorder.dto.ProductResponseDTO;
import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.enums.InventoryChangeType;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.service.InventoryService;
import com.raihan.nexaorder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Path PRODUCT_UPLOAD_DIR = Path.of("src/main/resources/static/uploads/products");
    private static final String PRODUCT_UPLOAD_URL = "/uploads/products/";
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        return toResponse(findProductOrThrow(id));
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = Product.builder()
                .name(normalizeText(request.getName()))
                .category(normalizeText(request.getCategory()))
                .price(normalizePrice(request.getPrice()))
                .stock(request.getStock())
                .description(normalizeDescription(request.getDescription()))
                .imageUrl(storeImage(request.getImageFile(), null))
                .build();

        Product savedProduct = productRepository.save(product);
        if (savedProduct.getStock() != null && savedProduct.getStock() > 0) {
            inventoryService.createInventoryLog(
                    savedProduct,
                    InventoryChangeType.IN,
                    savedProduct.getStock(),
                    "Initial stock for " + savedProduct.getName()
            );
        }

        return toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product product = findProductOrThrow(id);
        int previousStock = product.getStock() == null ? 0 : product.getStock();

        product.setName(normalizeText(request.getName()));
        product.setCategory(normalizeText(request.getCategory()));
        product.setPrice(normalizePrice(request.getPrice()));
        product.setStock(request.getStock());
        product.setDescription(normalizeDescription(request.getDescription()));
        product.setImageUrl(storeImage(request.getImageFile(), product.getImageUrl()));

        Product savedProduct = productRepository.save(product);
        int currentStock = savedProduct.getStock() == null ? 0 : savedProduct.getStock();
        int stockChange = currentStock - previousStock;

        if (stockChange != 0) {
            inventoryService.createInventoryLog(
                    savedProduct,
                    InventoryChangeType.ADJUSTMENT,
                    stockChange,
                    "Stock updated from " + previousStock + " to " + currentStock + " during product edit"
            );
        }

        return toResponse(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> searchProducts(String keyword, String category) {
        String normalizedKeyword = hasText(keyword) ? keyword.trim() : null;
        String normalizedCategory = hasText(category) ? category.trim() : null;

        if (normalizedKeyword == null && normalizedCategory == null) {
            return getAllProducts();
        }

        return productRepository.searchProducts(normalizedKeyword, normalizedCategory)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getLowStockProducts() {
        return productRepository.findByStockLessThanEqualOrderByStockAsc(LOW_STOCK_THRESHOLD)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return productRepository.findDistinctCategories();
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product was not found"));
    }

    private ProductResponseDTO toResponse(Product product) {
        return ProductResponseDTO.fromEntity(product, LOW_STOCK_THRESHOLD);
    }

    private String storeImage(MultipartFile imageFile, String existingImageUrl) {
        if (imageFile == null || imageFile.isEmpty()) {
            return existingImageUrl;
        }

        validateImage(imageFile);

        try {
            Files.createDirectories(PRODUCT_UPLOAD_DIR);
            String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename() == null
                    ? "product-image"
                    : imageFile.getOriginalFilename());
            String extension = getFileExtension(originalFilename);
            String storedFilename = UUID.randomUUID() + extension;
            Path targetPath = PRODUCT_UPLOAD_DIR.resolve(storedFilename).normalize();

            Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return PRODUCT_UPLOAD_URL + storedFilename;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not store product image");
        }
    }

    private void validateImage(MultipartFile imageFile) {
        if (imageFile.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Product image must not exceed 5 MB");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Product image must be JPG, PNG, WEBP, or GIF");
        }
    }

    private String getFileExtension(String filename) {
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex < 0) {
            return "";
        }

        return filename.substring(extensionIndex).toLowerCase(Locale.ROOT);
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeDescription(String value) {
        return hasText(value) ? value.trim() : null;
    }

    private BigDecimal normalizePrice(BigDecimal value) {
        return value == null ? null : value;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
