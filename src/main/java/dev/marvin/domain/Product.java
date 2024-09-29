package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_products")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String productId;
    @Column(unique = true)
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private byte[] imageBytes;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
