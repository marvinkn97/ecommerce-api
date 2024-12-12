package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.OrderResponse;

public interface IOrderService {
    String placeOrder(UserEntity userEntity);

    OrderResponse getOrder(String orderNumber);
}
