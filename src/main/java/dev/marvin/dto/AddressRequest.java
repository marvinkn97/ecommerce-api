package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "Address Request", name = "AddressRequest", description = "Data Transfer Object (DTO) for adding or updating an address")
public record AddressRequest(
        @Schema(
                description = "County of the address",
                example = "Nairobi"
        )
        @NotBlank(message = "County is required")
        @Size(min = 3, max = 50, message = "County must be between 3 and 50 characters")
        String county,

        @Schema(
                description = "Town of the address",
                example = "Westlands"
        )
        @NotBlank(message = "Town is required")
        @Size(min = 3, max = 50, message = "Town must be between 3 and 50 characters")
        String town,

        @Schema(
                description = "Street of the address",
                example = "Waiyaki Way"
        )
        @Size(max = 100, message = "Street must not exceed 100 characters")
        String street,

        @Schema(
                description = "Building of the address",
                example = "ABC Towers"
        )
        @Size(max = 100, message = "Building must not exceed 100 characters")
        String building
) {
}
