package dev.marvin.order;

import dev.marvin.shared.Status;
import dev.marvin.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "t_orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String orderNo;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Collection<OrderItem> orderItems;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Status status;
}
