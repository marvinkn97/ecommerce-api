package dev.marvin.auth;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_otp")
@Data
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mobile;
    private String otp;
    private LocalDateTime expiryTime;
}
