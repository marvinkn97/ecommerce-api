package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Category Request")
public record CategoryRequest(
        @SchemaProperty(name = "Category Name")
        @NotBlank(message = "Category Name must not be blank")
        @Size(min = 2, message = "Category Name must have at least 2 characters")
        String categoryName) {
}
