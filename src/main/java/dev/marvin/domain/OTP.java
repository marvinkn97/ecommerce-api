package dev.marvin.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_otp")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String emailOrMobile;
    private String otp;
    private LocalDateTime expiryTime;
}
