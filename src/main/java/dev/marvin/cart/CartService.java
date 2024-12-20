package dev.marvin.cart;

import dev.marvin.cart.CartResponse;
import dev.marvin.user.UserEntity;

public interface CartService {
    void addProductToCart(Integer productId, UserEntity userEntity);

    void reduceCartItemQuantity(Integer productId, UserEntity userEntity);

    void deleteCartItem(Integer productId, UserEntity userEntity);

    CartResponse getCart(UserEntity userEntity);

    void deleteAllCartItems(UserEntity userEntity);

}
