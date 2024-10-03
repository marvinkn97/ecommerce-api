package dev.marvin.repository;

import dev.marvin.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p")
    Page<Product> getProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> getProductsByCategoryId(@Param("categoryId") Integer categoryId);
}
