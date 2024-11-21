package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddToCartRequest;
import dev.marvin.dto.CartResponse;

public interface CartService {
    void addProductToCart(AddToCartRequest addToCartRequest, UserEntity userEntity);

    void updateCartItemQty();

    CartResponse getCart(UserEntity userEntity);
}
