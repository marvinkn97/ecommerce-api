package dev.marvin.order;

import dev.marvin.order.OrderResponse;
import dev.marvin.user.UserEntity;

public interface OrderService {
    String placeOrder(UserEntity userEntity);

    OrderResponse getOrder(String orderNumber);
}
