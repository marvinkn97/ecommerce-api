package dev.marvin.utils;

import dev.marvin.domain.OTP;
import dev.marvin.dto.PreAuthRequest;
import dev.marvin.repository.OtpRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
public class OtpUtils {
    private final OtpRepository otpRepository;
    public void generateAndSendOtp(PreAuthRequest preAuthRequest) {
        if(preAuthRequest.hasMobile()){
            // Generate OTP
            String otp = generateOtp();

            // Save OTP to DB or cache (with expiration) -start with db learn cache with redis later
            OTP otpEntity = new OTP();
            otpEntity.setOtp(otp);
            otpEntity.setEmailOrMobile(preAuthRequest.mobile());
            otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));// Expires in 5 min
            otpRepository.save(otpEntity);
        }
        // Send OTP via email or SMS
        //emailService.sendOtp(emailOrMobile, otp);
    }

    public boolean verifyOtp(String emailOrMobile, String otp) {
        // Retrieve the stored OTP from DB or cache
        OTP storedOtp = otpRepository.findByEmailOrMobile(emailOrMobile);

        // Check if OTP matches and is not expired
        if (storedOtp != null && storedOtp.getOtp().equals(otp) && !isOtpExpired(storedOtp)) {
            return true;
        }
        return false;
    }

    private boolean isOtpExpired(OTP otp) {
        return otp.getExpiryTime().isBefore(LocalDateTime.now());
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }
}
