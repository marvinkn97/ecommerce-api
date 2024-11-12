package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "PreAuthRequest",
        title = "Pre-Authentication Request",
        description = "DTO for pre-authentication containing the user's mobile number."
)
public record PreAuthRequest(
        @Pattern(regexp = "^254\\d{9}$", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        @Schema(
                description = "User's mobile number, must start with country code 254 and be followed by 9 digits.",
                example = "254712345678"
        )
        String mobile) {

    public boolean hasMobile() {
        return mobile != null && !mobile.isEmpty();
    }
}
