package dev.marvin.address;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Address Update Request", name = "AddressUpdateRequest", description = "Data Transfer Object (DTO) for updating an address")
public record AddressUpdateRequest(
        @Schema(
                description = "County of the address",
                example = "Nairobi"
        )
        String county,

        @Schema(
                description = "Town of the address",
                example = "Westlands"
        )
        String town,

        @Schema(
                description = "Street of the address",
                example = "Waiyaki Way"
        )
        String street,

        @Schema(
                description = "Building of the address",
                example = "ABC Towers"
        )
        String building
) {
}
