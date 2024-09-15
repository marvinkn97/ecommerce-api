package dev.marvin.dto;

import java.time.LocalDateTime;

public record CategoryResponse(
        Integer id,
        String categoryName,
        LocalDateTime createdAt
        ) {
}
