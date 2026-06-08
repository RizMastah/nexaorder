package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final SpringDataProductRepository delegate;

    public ProductRepositoryImpl(SpringDataProductRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Product save(Product product) {
        return delegate.save(product);
    }

    @Override
    public List<Product> saveAll(Iterable<Product> products) {
        return delegate.saveAll(products);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public void delete(Product product) {
        delegate.delete(product);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public List<Product> findByCategory(String category) {
        return delegate.findByCategory(category);
    }

    @Override
    public List<Product> findAllByOrderByCreatedAtDesc() {
        return delegate.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Product> findByCategoryIgnoreCaseOrderByNameAsc(String category) {
        return delegate.findByCategoryIgnoreCaseOrderByNameAsc(category);
    }

    @Override
    public List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String keyword) {
        return delegate.findByNameContainingIgnoreCaseOrderByNameAsc(keyword);
    }

    @Override
    public List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrderByNameAsc(String name, String category) {
        return delegate.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrderByNameAsc(name, category);
    }

    @Override
    public List<Product> findByStockLessThanEqualOrderByStockAsc(Integer threshold) {
        return delegate.findByStockLessThanEqualOrderByStockAsc(threshold);
    }

    @Override
    public long countByStockLessThanEqual(Integer threshold) {
        return delegate.countByStockLessThanEqual(threshold);
    }

    @Override
    public List<Product> searchProducts(String keyword, String category) {
        return delegate.searchProducts(keyword, category);
    }

    @Override
    public List<String> findDistinctCategories() {
        return delegate.findDistinctCategories();
    }
}
