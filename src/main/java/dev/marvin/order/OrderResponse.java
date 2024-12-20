package dev.marvin.order;

import dev.marvin.user.CustomerResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public record OrderResponse(
        String orderNo,
        CustomerResponse customerResponse,
        Collection<OrderItemResponse> orderItems,
        BigDecimal totalAmount,
        LocalDateTime orderDate,
        String orderStatus
) {
}
