package dev.marvin.repository;

import dev.marvin.domain.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OTP, Integer> {
    @Query("SELECT otp FROM OTP otp WHERE otp.emailOrMobile = :emailOrMobile")
    OTP findByEmailOrMobile(@Param("emailOrMobile") String emailOrMobile);
}
