package dev.marvin.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        title = "Password Change Request",
        description = "Request DTO for changing the user's password"
)
public record PasswordChangeRequest(
        @NotBlank(message = "Previous password is required")
        @Schema(
                description = "User's previous password.",
                example = "PreviousPassword123"
        )
        String previousPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        @Schema(
                description = "User's new password, which must be at least 8 characters",
                example = "NewSecurePassword123"
        )
        String newPassword,

        @NotBlank(message = "Confirm new password is required")
        @Schema(
                description = "Confirmation of the new password to ensure accuracy.",
                example = "NewSecurePassword123"
        )
        String confirmNewPassword
) {
    public boolean isNewPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmNewPassword);
    }

    public boolean isNewPasswordSameAsOld(){
        return previousPassword != null && !previousPassword.equals(newPassword);
    }
}
