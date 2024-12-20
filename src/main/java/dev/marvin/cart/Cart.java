package dev.marvin.cart;

import dev.marvin.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "t_carts")
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    Collection<CartItem> cartItems = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
