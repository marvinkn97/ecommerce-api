package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.CartResponse;

public interface ICartService {
    void addProductToCart(Integer productId, UserEntity userEntity);

    void reduceCartItemQuantity(Integer productId, UserEntity userEntity);

    void deleteCartItem(Integer productId, UserEntity userEntity);

    CartResponse getCart(UserEntity userEntity);

    void deleteAllCartItems(UserEntity userEntity);

}
