package dev.marvin.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(title = "Product Update Request", name = "ProductUpdateRequest", description = "DTO for updating product details")
public record ProductUpdateRequest(

        @Schema(
                description = "Name of the product",
                example = "Wireless Mouse"
        )
        @Size(min = 3, max = 100, message = "Product Name must be between 3 and 100 characters")
        String productName,

        @Schema(
                description = "Price of the product",
                example = "19.99"
        )
        @Positive(message = "Product Price must be a positive number")
        BigDecimal productPrice,

        @Schema(
                description = "Discounted price of the product (if applicable)",
                example = "15.99"
        )
        @PositiveOrZero(message = "Discount Price must be zero or a positive number")
        BigDecimal discountPrice,

        @Schema(
                description = "Description of the product",
                example = "A high-quality wireless mouse with ergonomic design."
        )
        @Size(max = 500, message = "Product Description must not exceed 500 characters")
        String productDescription,

        @Schema(
                description = "Available quantity of the product",
                example = "10"
        )
        @Positive(message = "Product Quantity must be a positive number")
        Integer productQuantity
) {
}
