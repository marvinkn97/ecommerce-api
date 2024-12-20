package dev.marvin.order;

import dev.marvin.product.ProductResponse;

public record OrderItemResponse(ProductResponse productResponse, Integer noOfUnits) {
}
