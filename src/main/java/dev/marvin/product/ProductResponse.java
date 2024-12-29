package dev.marvin.product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer productId,
        String productName,
        BigDecimal productPrice,
        BigDecimal specialPrice,
        Integer availableQty,
        String productDescription,
        String status) {
}
