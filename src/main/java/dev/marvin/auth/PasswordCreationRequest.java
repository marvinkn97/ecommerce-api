package dev.marvin.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        title = "Password Creation Request",
        name = "PasswordCreationRequest",
        description = "Request DTO for creating a user password.")
public record PasswordCreationRequest(
        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = "^254\\d{9}$", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        @Schema(
                description = "User's mobile number in the format starting with country code 254 and followed by 9 digits.",
                example = "254712345678"
        )
        String mobile,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Schema(
                description = "Password for user account. Must be at least 8 characters long.",
                example = "StrongPass123"
        )
        String password,

        @NotBlank(message = "Confirm Password is required")
        @Size(min = 8, message = "Confirm Password must be at least 8 characters long")
        @Schema(
                description = "Confirmation of the password. Must match the password and be at least 8 characters long.",
                example = "StrongPass123"
        )
        String confirmPassword

) {

    public boolean passwordMatch() {
        return password.equals(confirmPassword);
    }
}
