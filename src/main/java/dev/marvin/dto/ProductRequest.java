package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(title = "Product Request", name = "ProductRequest", description = "Data Transfer Object (DTO) for creating or updating a product")
public record ProductRequest(

        @Schema(
                description = "ID of the category to which the product belongs",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be a positive number")
        Integer categoryId,

        @Schema(
                description = "Name of the product",
                example = "Wireless Mouse",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Product Name is required")
        @Size(min = 3, max = 100, message = "Product Name must be between 3 and 100 characters")
        String productName,

        @Schema(
                description = "Price of the product",
                example = "19.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Product Price is required")
        @Positive(message = "Product Price must be a positive number")
        BigDecimal productPrice,

        @Schema(
                description = "Discounted price of the product (if applicable)",
                example = "15.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @PositiveOrZero(message = "Discount Price must be zero or a positive number")
        BigDecimal discountPrice,

        @Schema(
                description = "Description of the product",
                example = "A high-quality wireless mouse with ergonomic design.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Size(max = 500, message = "Product Description must not exceed 500 characters")
        String productDescription,

        @Schema(
                description = "Available quantity of the product",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Product Quantity is required")
        @Positive(message = "Product Quantity must be a positive number")
        Integer productQuantity
) {
}
