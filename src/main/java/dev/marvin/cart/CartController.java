package dev.marvin.cart;

import dev.marvin.shared.MessageConstants;
import dev.marvin.shared.ResponseDto;
import dev.marvin.user.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("${app.api.prefix}/cart")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cart Resource", description = "CRUD operations for Cart Management")
public class CartController {
    private final CartService cartService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") //change later
    @Operation(
            summary = "Add a product to cart",
            description = "Add a product to the logged-in user's cart by providing the product ID."
    )
    @ApiResponse(responseCode = "200", description = "Product added to cart successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")
    public ResponseEntity<ResponseDto<String>> add(@RequestParam("productId") Integer productId) {
        log.info("Inside add method of CartController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        cartService.addProductToCart(Objects.requireNonNull(productId), userPrincipal.userEntity());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.ADD_PRODUCT_TO_CART));
    }

    @PutMapping("/reduce")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") //change later
    @Operation(
            summary = "Reduce Cart Item Quantity",
            description = "Decrements the quantity of a product in the user's cart"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity reduced successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")
    })
    public ResponseEntity<ResponseDto<String>> reduceCartItemQuantity(@RequestParam("productId") Integer productId) {
        log.info("Inside reduceCartItemQuantity method of CartController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        cartService.reduceCartItemQuantity(Objects.requireNonNull(productId), userPrincipal.userEntity());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.CART_ITEM_UPDATED));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") //change later
    @Operation(
            summary = "Delete Product from Cart",
            description = "Removes a product from the user's cart completely."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")
    })
    public ResponseEntity<ResponseDto<String>> deleteProductFromCart(@RequestParam Integer productId) {
        log.info("Inside reduceCartItemQuantity method of CartController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        cartService.deleteCartItem(Objects.requireNonNull(productId), userPrincipal.userEntity());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.CART_ITEM_DELETED));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") //change later
    @Operation(
            summary = "Retrieve Cart Details",
            description = "Fetches the cart details of the currently authenticated user, including cart items and total value.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart details retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected error occurred when processing request")
            }

    )
    public ResponseEntity<ResponseDto<Object>> getCart() {
        log.info("Inside getCart method of CartController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), cartService.getCart(userPrincipal.userEntity())));
    }
}
