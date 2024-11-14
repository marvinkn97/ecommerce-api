package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(
        title = "User Profile Update Request",
        name = "UserProfileUpdateRequest",
        description = "Request DTO for updating user profile details.")
public record UserProfileUpdateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Schema(
                description = "Full name of the user, between 2 and 50 characters.",
                example = "John Doe"
        )
        String name
) {
}
