package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        title = "Authentication Request",
        name = "AuthenticationRequest",
        description = "Request DTO for user authentication using mobile number and password.")
public record AuthenticationRequest(

        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = "^254\\d{9}$", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        @Schema(
                description = "User's mobile number in the format starting with country code 254 followed by 9 digits.",
                example = "254712345678"
        )
        String mobile,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Schema(
                description = "User's password. Must be at least 8 characters long.",
                example = "StrongPassword123"
        )
        String password
) {
}
