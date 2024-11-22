package dev.marvin.dto;

import java.math.BigDecimal;
import java.util.Collection;

public record CartResponse(
        String ownerId,
        Collection<CartItemResponse> cartItems,
        BigDecimal totalAmount
) {
}
