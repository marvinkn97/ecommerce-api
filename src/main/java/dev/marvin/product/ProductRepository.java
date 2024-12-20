package dev.marvin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p")
    Collection<Product> getProducts();

    @Query("SELECT p FROM Product p")
    Page<Product> getProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> getProductsByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :productId")
    void reduceProductStock(@Param("productId") Integer productId, @Param("quantity") int quantity);

}
