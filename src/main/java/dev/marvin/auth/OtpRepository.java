package dev.marvin.repository;

import dev.marvin.domain.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OTP, Integer> {
    @Query("SELECT o FROM OTP o WHERE o.mobile = :mobile AND o.otp = :otp")
    OTP findByMobileAndOtp(@Param("mobile") String mobile, @Param("otp") String otp);
}
