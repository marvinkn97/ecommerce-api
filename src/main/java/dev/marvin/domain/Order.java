package dev.marvin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_orders")
@Data
public class Order {
    @Id
    private Integer orderId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Integer createdBy;
}
