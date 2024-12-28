package dev.marvin.shared;

import dev.marvin.address.Address;
import dev.marvin.address.AddressResponse;
import dev.marvin.cart.Cart;
import dev.marvin.cart.CartItem;
import dev.marvin.cart.CartItemResponse;
import dev.marvin.cart.CartResponse;
import dev.marvin.order.Order;
import dev.marvin.order.OrderItem;
import dev.marvin.order.OrderItemResponse;
import dev.marvin.order.OrderResponse;
import dev.marvin.product.Product;
import dev.marvin.product.ProductResponse;
import dev.marvin.user.CustomerResponse;
import dev.marvin.user.UserEntity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

public class Mapper {
    private Mapper() {
    }

    public static ProductResponse mapToDto(Product product) {
        BigDecimal specialPrice = product.getPrice().subtract(product.getDiscountPrice());
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), specialPrice, product.getQuantity(), product.getDescription(), product.getStatus().name());
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
