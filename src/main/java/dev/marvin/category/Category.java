package dev.marvin.category;

import dev.marvin.product.Product;
import dev.marvin.shared.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "t_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    Collection<Product> products = new HashSet<>();
}
