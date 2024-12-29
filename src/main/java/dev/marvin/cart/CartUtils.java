package dev.marvin.cart;

import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.product.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartUtils {
    private final CartRepository cartRepository;
    private final ProductUtils productUtils;
    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }
}
