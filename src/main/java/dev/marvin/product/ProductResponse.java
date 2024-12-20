package dev.marvin.product;

import java.math.BigDecimal;

public record ProductResponse(Integer id, String name, BigDecimal price, BigDecimal specialPrice, Integer availableQty,
                              String description, String status) {
}
