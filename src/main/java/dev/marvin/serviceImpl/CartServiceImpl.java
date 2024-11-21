package dev.marvin.serviceImpl;

import dev.marvin.domain.Cart;
import dev.marvin.domain.CartItem;
import dev.marvin.domain.Product;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddToCartRequest;
import dev.marvin.dto.CartResponse;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.repository.CartRepository;
import dev.marvin.service.CartService;
import dev.marvin.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final ProductUtils productUtils;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public void addProductToCart(AddToCartRequest addToCartRequest, UserEntity userEntity) {
        log.info("Inside addProductToCart method of CartServiceImpl");
        Integer productId = addToCartRequest.productId();
        Integer userId = userEntity.getId();

        Product product = productUtils.getProductById(productId);
        log.info("product: {}", product);

        if (product.getProductQuantity() <= 0) {
            throw new RequestValidationException("product out of stock");
        }

        // Fetch user's cart or create a new one
        Cart cart = cartRepository.findByUserId(userId).orElseGet(Cart::new);
        cart.setUserEntity(userEntity);

        // Check if product already in the cart
        Optional<CartItem> existingCartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Increment quantity if product exists in the cart
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);

        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
        log.info("Product added to cart successfully");
    }

    @Override
    public void updateCartItemQty() {

    }

    @Override
    public CartResponse getCart(UserEntity userEntity) {
        return null;
    }
}