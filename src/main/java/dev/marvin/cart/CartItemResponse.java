package dev.marvin.cart;

import dev.marvin.product.ProductResponse;

public record CartItemResponse(ProductResponse productResponse, Integer noOfUnits) {
}
