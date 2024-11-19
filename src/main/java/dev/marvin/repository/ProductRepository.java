package dev.marvin.repository;

import dev.marvin.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    Collection<Product> getProducts();

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    Page<Product> getProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id = :pId AND p.isDeleted = false")
    Optional<Product> getProductById(@Param("pId") Integer productId);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isDeleted = false")
    Page<Product> getProductsByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);
}
