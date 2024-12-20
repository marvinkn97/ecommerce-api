package dev.marvin.user;

import dev.marvin.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "t_users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, name = "mobile_number")
    private String mobileNumber;

    private String fullName;
    private String password;
    private Date dateOfBirth;
    private Boolean isFullyRegistered = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum roleEnum;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "userEntity")
    private Address address;
}
