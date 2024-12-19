package dev.marvin.dto;

import dev.marvin.product.ProductResponse;

public record OrderItemResponse(ProductResponse productResponse, Integer noOfUnits) {
}
