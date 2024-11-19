package dev.marvin.mapper;

import dev.marvin.domain.Category;
import dev.marvin.domain.Product;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.dto.ProductResponse;

import java.math.BigDecimal;

public class Mapper {
    private Mapper() {
    }

    public static CategoryResponse mapToDto(Category category) {
        return new CategoryResponse(category.getId(), category.getCategoryName(), category.getStatus().name(), category.getCreatedAt());
    }

    public static ProductResponse mapToDto(Product product){
        BigDecimal specialPrice = product.getProductPrice().subtract(product.getDiscountPrice());
        return new ProductResponse(product.getId(), product.getProductName(), product.getProductPrice(), specialPrice, product.getProductQuantity(), product.getProductDescription());
    }
}
