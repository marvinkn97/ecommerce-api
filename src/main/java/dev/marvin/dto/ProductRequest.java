package dev.marvin.dto;

import java.math.BigDecimal;

public record ProductRequest(
        Integer categoryId,
        String productName,
        BigDecimal productPrice,
        String productDescription,
        byte[] imageBytes
        ) {
}