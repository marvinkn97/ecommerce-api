package dev.marvin.service;

import dev.marvin.domain.Cart;
import dev.marvin.domain.CartItem;
import dev.marvin.domain.Product;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.CartResponse;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.CartRepository;
import dev.marvin.utils.CartUtils;
import dev.marvin.utils.Mapper;
import dev.marvin.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService implements ICartService {
    private final ProductUtils productUtils;
    private final CartUtils cartUtils;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public void addProductToCart(Integer productId, UserEntity userEntity) {
        log.info("Inside addProductToCart method of CartServiceImpl");
        Integer userId = userEntity.getId();

        Product product = productUtils.getProductById(productId);
        log.info("product: {}", product);

        if (product.getQuantity() <= 0) {
            throw new RequestValidationException("product out of stock");
        }

        // Fetch user's cart or create a new one
        Cart cart = cartRepository.findByUserId(userId).orElseGet(Cart::new);
        cart.setUserEntity(userEntity);

        // Check if product already in the cart
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        // Increment quantity if product exists
                        cartItem -> cartItem.setQuantity(cartItem.getQuantity() + 1),
                        // Otherwise, add new product to cart
                        () -> {
                            CartItem newCartItem = new CartItem();
                            newCartItem.setProduct(product);
                            newCartItem.setQuantity(1);
                            newCartItem.setCart(cart);
                            cart.getCartItems().add(newCartItem);
                        }
                );
        cartRepository.save(cart);
        log.info("Product added to cart successfully");
    }

    @Override
    public void reduceCartItemQuantity(Integer productId, UserEntity userEntity) {
        log.info("Inside reduceCartItemQuantity method of CartServiceImpl");

        // Fetch user's cart
        Cart cart = cartUtils.getCartByUserId(userEntity.getId());

        // Find the product in the cart
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        if (cartItem.getQuantity() <= 1) {
            log.error("Cannot decrement quantity: Quantity is already 1 or less for product ID {}", productId);
            throw new RequestValidationException("Cannot decrement quantity further. Please delete the item instead");
        }

        // Reduce quantity by 1
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartRepository.save(cart);
        log.info("Product quantity decremented successfully");
    }

    @Override
    @Transactional
    public void deleteCartItem(Integer productId, UserEntity userEntity) {
        log.info("Inside reduceCartItemQuantity method of CartServiceImpl");

        // Fetch user's cart
        Cart cart = cartUtils.getCartByUserId(userEntity.getId());

        // Find the product in the cart
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        // Remove the cart item
        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
        log.info("Product with ID {} removed from cart successfully", productId);
    }

    @Override
    public CartResponse getCart(UserEntity userEntity) {
        log.info("Inside getCart method of CartServiceImpl");
        Cart cart = cartUtils.getCartByUserId(userEntity.getId());
        return Mapper.mapToDto(cart);
    }

    @Override
    public void deleteAllCartItems(UserEntity userEntity) {

    }
}