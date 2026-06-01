package com.raihan.nexaorder.repository;

import com.raihan.nexaorder.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findAllByOrderByCreatedAtDesc();

    List<Product> findByCategoryIgnoreCaseOrderByNameAsc(String category);

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String keyword);

    List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrderByNameAsc(String name, String category);

    List<Product> findByStockLessThanEqualOrderByStockAsc(Integer threshold);

    @Query("""
            select p from Product p
            where (:keyword is null
                or lower(p.name) like lower(concat('%', :keyword, '%'))
                or lower(p.category) like lower(concat('%', :keyword, '%')))
              and (:category is null or lower(p.category) = lower(:category))
            order by p.name asc
            """)
    List<Product> searchProducts(@Param("keyword") String keyword, @Param("category") String category);

    @Query("select distinct p.category from Product p order by p.category")
    List<String> findDistinctCategories();
}
