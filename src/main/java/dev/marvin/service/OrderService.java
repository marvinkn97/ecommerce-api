package dev.marvin.service;

import dev.marvin.domain.*;
import dev.marvin.dto.OrderResponse;
import dev.marvin.dto.SmsRequest;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.*;
import dev.marvin.utils.CartUtils;
import dev.marvin.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {
    private final CartUtils cartUtils;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ISmsService smsService;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public String placeOrder(UserEntity userEntity) {
        log.info("Inside placeOrder method of OrderServiceImpl");
        Cart cart = cartUtils.getCartByUserId(userEntity.getId());

        Collection<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new RequestValidationException("Cart is empty");
        }

        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString());
        order.setUser(userEntity);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Status.ORDER_PLACED);
        Order savedOrder = orderRepository.save(order);

        Collection<OrderItem> orderItems = new HashSet<>();
        for(CartItem cartItem: cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(savedOrder);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
         orderItemRepository.saveAll(orderItems);

        cartItems.forEach(cartItem -> {
            Product product = cartItem.getProduct();

            //reduce product stock
            productRepository.reduceProductStock(product.getId(), cartItem.getQuantity());
        });

        cartItemRepository.deleteAllByCartId(cart.getId());
        cartRepository.deleteCartByUser(userEntity.getId());

        String orderNo = savedOrder.getOrderNo();

        log.info("Order placed successfully with order number: {}", orderNo);
        smsService.sendSms(new SmsRequest(userEntity.getMobileNumber(), "TIARACONECT", "Order placed successfully. Your order number is %s".formatted(orderNo)));
        return orderNo;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderNumber) {
        log.info("Inside getOrder method of OrderServiceImpl");
        Order order = orderRepository.getOrder(orderNumber).orElseThrow(()-> new ResourceNotFoundException("Order not found"));
        return Mapper.mapToDto(order);
    }
}
