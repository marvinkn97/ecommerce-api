package dev.marvin.dto;

import dev.marvin.product.ProductResponse;

public record CartItemResponse(ProductResponse productResponse, Integer noOfUnits) {
}
