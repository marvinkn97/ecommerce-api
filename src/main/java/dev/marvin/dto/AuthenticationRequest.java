package dev.marvin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(

        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = "254[0-9]{9}", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        String mobile,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password) {
}
