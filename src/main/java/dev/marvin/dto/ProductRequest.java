package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(name = "Product Request", description = "Product Request Structure")
public record ProductRequest(

        @SchemaProperty(name = "Category ID")
        @NotNull(message = "Category ID must not be null")
        @Positive(message = "Category ID must be a positive number")
        Integer categoryId,

        @SchemaProperty(name = "Product Name")
        @NotBlank(message = "Product Name must not be blank")
        @Size(min = 3, message = "Product Name must have at least 3 characters")
        String productName,

        @SchemaProperty(name = "Product Price")
        @NotNull(message = "Product Price must not be null")
        @PositiveOrZero(message = "Product Price must be a positive number")
        BigDecimal productPrice,

        BigDecimal discountPrice,
        BigDecimal specialPrice,
        String productDescription,
        String encodedProductImage
) {
}