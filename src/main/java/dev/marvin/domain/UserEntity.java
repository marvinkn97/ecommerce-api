package dev.marvin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "VARCHAR", length = 50)
    private String fullName;
    @Column(unique = true, columnDefinition = "VARCHAR", length = 50)
    private String email;
    private String password;
    @Column(unique = true, columnDefinition = "VARCHAR", length = 12)
    private String mobileNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
