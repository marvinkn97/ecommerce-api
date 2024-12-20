package dev.marvin.auth;

import dev.marvin.exception.RequestValidationException;
import dev.marvin.notification.sms.SmsRequest;
import dev.marvin.notification.sms.SmsService;
import dev.marvin.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpUtils {
    private final OtpRepository otpRepository;
    private final SmsService smsService;
    private final UserService userService;

    @Transactional
    @Async
    public void generateAndSendOtp(PreAuthRequest preAuthRequest) {
            if (preAuthRequest.hasMobile()) {
                // Generate OTP
                String otp = generateOtp();

                // Save OTP to DB or cache (with expiration) - start with db learn cache with redis later
                OTP otpEntity = new OTP();
                otpEntity.setOtp(otp);
                otpEntity.setMobile(preAuthRequest.mobile());

                long duration = 5L;

                otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(duration));// Expires in [duration] min
                OTP savedOtp = otpRepository.save(otpEntity);

                // Send OTP via SMS
                String message = """
                        Dear customer.
                        Your OTP code is %s
                        The code is valid for %s minutes
                        """.formatted(savedOtp.getOtp(), duration);
                SmsRequest smsRequest = new SmsRequest(preAuthRequest.mobile(), "TIARACONECT", message);
                smsService.sendSms(smsRequest);
            }
    }

    public void verifyOtp(OtpVerificationRequest otpVerificationRequest) {

        if (!otpVerificationRequest.isValid()) {
            throw new RequestValidationException("Bad Request");
        }

        // Retrieve the stored OTP from DB or cache
        OTP storedOtp = otpRepository.findByMobileAndOtp(otpVerificationRequest.mobile(), otpVerificationRequest.otp());

        // Check if OTP exists
        if (storedOtp == null) {
            throw new RequestValidationException("OTP does not exist");
        }

        // Check if OTP is expired
        if (isOtpExpired(storedOtp)) {
            throw new RequestValidationException("OTP is expired");
        }

        // Check if OTP matches
        if (!storedOtp.getOtp().equals(otpVerificationRequest.otp())) {
            throw new RequestValidationException("OTP is invalid");
        }

        // Optionally, delete OTP after successful verification to prevent reuse
        otpRepository.delete(storedOtp);
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
