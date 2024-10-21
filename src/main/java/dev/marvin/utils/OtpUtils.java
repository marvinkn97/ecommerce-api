package dev.marvin.utils;

import dev.marvin.domain.OTP;
import dev.marvin.dto.PreAuthRequest;
import dev.marvin.dto.SmsRequest;
import dev.marvin.repository.OtpRepository;
import dev.marvin.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OtpUtils {
    private final OtpRepository otpRepository;
    private final SmsService smsService;

    public void generateAndSendOtp(PreAuthRequest preAuthRequest) {
        if (preAuthRequest.hasMobile()) {
            // Generate OTP
            String otp = generateOtp();

            // Save OTP to DB or cache (with expiration) -start with db learn cache with redis later
            OTP otpEntity = new OTP();
            otpEntity.setOtp(otp);
            otpEntity.setMobile(preAuthRequest.mobile());
            otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));// Expires in 5 min
            OTP savedOtp = otpRepository.save(otpEntity);

            // Send OTP via SMS
            String message = """
                    Dear customer.
                    Your OTP code is %s
                    The code is valid for %s minutes
                    """.formatted(savedOtp.getOtp(), savedOtp.getExpiryTime().getMinute());
            SmsRequest smsRequest = new SmsRequest(preAuthRequest.mobile(), "TIARACONECT", message);
            smsService.sendSms(smsRequest);
        }

    }

    public boolean verifyOtp(String emailOrMobile, String otp) {
        // Retrieve the stored OTP from DB or cache
        OTP storedOtp = otpRepository.findByMobile(emailOrMobile);

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
