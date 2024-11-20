package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddToCartRequest;
import dev.marvin.dto.ResponseDto;

public interface CartService {
    ResponseDto<String> addProductToCart(AddToCartRequest addToCartRequest, UserEntity userEntity);

    ResponseDto<Object> getCart(UserEntity userEntity);
}
