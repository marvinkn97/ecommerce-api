package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "t_products")
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer version;
    @Column(unique = true)
    private String name;
    private String brand;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String description;
    private Integer quantity;
    private Boolean isDeleted = Boolean.FALSE;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, orphanRemoval = true)
    private Collection<Image> images = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
