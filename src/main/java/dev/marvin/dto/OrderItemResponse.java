package dev.marvin.dto;

public record OrderItemResponse(ProductResponse productResponse, Integer noOfUnits) {
}
