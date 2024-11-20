package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.Cart;
import dev.marvin.domain.CartItem;
import dev.marvin.domain.Product;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddToCartRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.exception.ServiceException;
import dev.marvin.repository.CartRepository;
import dev.marvin.repository.ProductRepository;
import dev.marvin.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public ResponseDto<String> addProductToCart(AddToCartRequest addToCartRequest, UserEntity userEntity) {
        log.info("Inside addProductToCart method of CartServiceImpl");
        try {
            Integer productId = addToCartRequest.productId();
            Integer userId = userEntity.getId();

            Product product = productRepository.getProductById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id [%s] not found".formatted(productId)));
            log.info("product: {}", product);

            if(product.getProductQuantity() <= 0){
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
            return new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "Product added to cart successfully");
        } catch (ResourceNotFoundException | RequestValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in addProductToCart: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public ResponseDto<Object> getCart(UserEntity userEntity) {
        return null;
    }
}