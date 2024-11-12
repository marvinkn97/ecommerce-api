package dev.marvin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Date;

@Schema(
        title = "User Profile Request",
        name = "UserProfileRequest",
        description = "Request DTO for completing user profile details.")
public record UserProfileRequest(

        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = "^254\\d{9}$", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        @Schema(
                description = "User's mobile number in the format starting with country code 254 followed by 9 digits.",
                example = "254712345678"
        )
        String mobile,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Schema(
                description = "Full name of the user, between 2 and 50 characters.",
                example = "John Doe"
        )
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be a past date")
        @Schema(
                description = "User's date of birth in the format 'dd-MM-yyyy'. Must be a past date.",
                example = "01-01-1990",
                type = "string",
                format = "date"
        )
        Date dateOfBirth,

        @NotNull(message = "Terms and conditions acceptance is required")
        @Schema(
                description = "Indicates whether the user has accepted the terms and conditions.",
                example = "true"
        )
        Boolean termsAndConditions

) {
}
