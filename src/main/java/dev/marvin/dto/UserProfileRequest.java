package dev.marvin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.util.Date;

public record UserProfileRequest(

        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = "254[0-9]{9}", message = "Provide a valid mobile number starting with 254 and followed by 9 digits")
        String mobile,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be a past date")
        Date dateOfBirth,
        @NotNull(message = "Terms and conditions acceptance is required")
        Boolean termsAndConditions
         ) {
}
