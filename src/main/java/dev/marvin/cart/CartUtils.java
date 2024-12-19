package dev.marvin.utils;

import dev.marvin.cart.Cart;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartUtils {
    private final CartRepository cartRepository;
    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }
}
