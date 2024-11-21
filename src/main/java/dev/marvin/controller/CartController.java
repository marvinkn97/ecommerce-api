package dev.marvin.controller;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.dto.AddToCartRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cart")
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
    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody AddToCartRequest addToCartRequest){
        log.info("Inside add method of CartController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        cartService.addProductToCart(addToCartRequest, userPrincipal.userEntity());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.ADD_PRODUCT_TO_CART));

    }
}
