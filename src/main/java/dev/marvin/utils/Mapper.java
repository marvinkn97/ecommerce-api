package dev.marvin.utils;

import dev.marvin.domain.Cart;
import dev.marvin.domain.CartItem;
import dev.marvin.domain.Category;
import dev.marvin.domain.Product;
import dev.marvin.dto.CartItemResponse;
import dev.marvin.dto.CartResponse;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

public class Mapper {
    private Mapper() {
    }

    public static CategoryResponse mapToDto(Category category) {
        return new CategoryResponse(category.getId(), category.getCategoryName(), category.getStatus().name(), category.getCreatedAt());
    }

    public static ProductResponse mapToDto(Product product) {
        BigDecimal specialPrice = product.getProductPrice().subtract(product.getDiscountPrice());
        return new ProductResponse(product.getId(), product.getProductName(), product.getProductPrice(), specialPrice, product.getProductQuantity(), product.getProductDescription());
    }

    public static CartItemResponse mapToDto(CartItem cartItem) {
        return new CartItemResponse(mapToDto(cartItem.getProduct()), cartItem.getQuantity());
    }

    public static CartResponse mapToDto(Cart cart) {
        Collection<CartItemResponse> cartItems = cart.getCartItems().stream().map(Mapper::mapToDto).collect(Collectors.toSet());

        BigDecimal totalAmount = cart.getCartItems()
                .stream()
                .map(cartItem -> cartItem.getProduct().getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getUserEntity().getMobileNumber(), cartItems, totalAmount);
    }
}
