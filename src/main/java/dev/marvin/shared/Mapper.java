package dev.marvin.utils;

import dev.marvin.category.Category;
import dev.marvin.category.CategoryResponse;
import dev.marvin.domain.*;
import dev.marvin.dto.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

public class Mapper {
    private Mapper() {
    }

    public static CategoryResponse mapToDto(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getStatus().name(), category.getCreatedAt());
    }

    public static ProductResponse mapToDto(Product product) {
        BigDecimal specialPrice = product.getPrice().subtract(product.getDiscountPrice());
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), specialPrice, product.getQuantity(), product.getDescription());
    }

    public static CartItemResponse mapToDto(CartItem cartItem) {
        return new CartItemResponse(mapToDto(cartItem.getProduct()), cartItem.getQuantity());
    }

    public static CartResponse mapToDto(Cart cart) {
        Collection<CartItemResponse> cartItems = cart.getCartItems().stream().map(Mapper::mapToDto).collect(Collectors.toSet());

        BigDecimal totalAmount = cart.getCartItems()
                .stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getUserEntity().getMobileNumber(), cartItems, totalAmount);
    }

    public static AddressResponse mapToDto(Address address) {
        return new AddressResponse(address.getCounty(), address.getTown(), address.getStreet(), address.getBuilding());
    }

    public static CustomerResponse mapToDto(UserEntity userEntity) {
        return new CustomerResponse(userEntity.getFullName(), userEntity.getMobileNumber(), mapToDto(userEntity.getAddress()));
    }

    public static OrderItemResponse mapToDto(OrderItem orderItem) {
        return new OrderItemResponse(mapToDto(orderItem.getProduct()), orderItem.getQuantity());
    }

    public static OrderResponse mapToDto(Order order) {
        CustomerResponse customer = mapToDto(order.getUser());

        Collection<OrderItemResponse> orderItems = order.getOrderItems()
                .stream().map(Mapper::mapToDto)
                .collect(Collectors.toSet());

        BigDecimal totalAmount = order.getOrderItems()
                .stream()
                .map(orderItem -> orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderResponse(order.getOrderNo(), customer, orderItems, totalAmount, order.getCreatedAt(), order.getStatus().name());
    }
}
