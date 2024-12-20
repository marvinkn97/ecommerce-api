package dev.marvin.category;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CategoryResponse(
        Integer id,
        String categoryName,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
        ) {
}
