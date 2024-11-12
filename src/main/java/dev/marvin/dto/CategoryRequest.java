package dev.marvin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        title = "Category Request",
        name = "CategoryRequest",
        description = "Data Transfer Object (DTO) for creating a new category"
)
public record CategoryRequest(
        @NotBlank(message = "Category Name must not be blank")
        @Size(min = 3, message = "Category Name must have at least 3 characters")
        @Schema(
                description = "The name of the category to be created.",
                example = "Electronics"
        )
        String categoryName) {
}
