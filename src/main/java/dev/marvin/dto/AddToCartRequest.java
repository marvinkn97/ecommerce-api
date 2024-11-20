package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(title = "Add To Cart Request", name = "AddToCartRequest", description = "Data Transfer Object (DTO) for adding a product to cart")
public record AddToCartRequest(

        @Schema(
                description = "ID of the product to add to cart",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be a positive number")
        Integer productId
) {
}
