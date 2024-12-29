package dev.marvin.cart;

import dev.marvin.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "t_carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    Collection<CartItem> cartItems = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
