package dev.marvin.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(
        title = "OTP Verification Request",
        name = "OtpVerificationRequest",
        description = "Request DTO for verifying a user's OTP during authentication."
)
public record OtpVerificationRequest(

        @Pattern(regexp = "^254\\d{9}$", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        @Schema(
                description = "User's mobile number, must start with the country code 254 and be followed by 9 digits.",
                example = "254712345678"
        )
        String mobile,

        @NotBlank(message = "OTP must be provided")
        @Min(value = 6, message = "6 Digits required")
        @Schema(
                description = "One-time password (OTP) for verification, must be exactly 6 digits.",
                example = "123456"
        )
        String otp

) {
    public boolean hasMobile() {
        return mobile != null && !mobile.isEmpty();
    }

    public boolean hasOtp() {
        return otp != null && !otp.isEmpty();
    }

    public boolean isValid() {
        return hasOtp() && hasMobile();
    }

}
