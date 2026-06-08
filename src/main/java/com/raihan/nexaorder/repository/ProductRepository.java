package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    List<Product> saveAll(Iterable<Product> products);

    Optional<Product> findById(Long id);

    void delete(Product product);

    long count();

    List<Product> findByCategory(String category);

    List<Product> findAllByOrderByCreatedAtDesc();

    List<Product> findByCategoryIgnoreCaseOrderByNameAsc(String category);

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String keyword);

    List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrderByNameAsc(String name, String category);

    List<Product> findByStockLessThanEqualOrderByStockAsc(Integer threshold);

    long countByStockLessThanEqual(Integer threshold);

    List<Product> searchProducts(String keyword, String category);

    List<String> findDistinctCategories();
}
