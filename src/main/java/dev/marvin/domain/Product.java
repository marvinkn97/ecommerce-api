package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_products")
@Data
public class Product {
    @Id
    private Integer id;
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private byte[] imageBytes;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
}
