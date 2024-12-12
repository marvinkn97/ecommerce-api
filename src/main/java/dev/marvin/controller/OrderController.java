package dev.marvin.controller;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.ResponseDto;
import dev.marvin.service.IOrderService;
import dev.marvin.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Resource", description = "CRUD Operations for Order Management")
public class OrderController {
    private final AuthUtils authUtils;
    private final IOrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Place a new order",
            description = "This endpoint allows the authenticated user to place an order for the items in their cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g., cart is empty)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user must be authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDto<String>> placeOrder() {
        log.info("Inside placeOrder method of OrderController");
        UserEntity userEntity = authUtils.getAuthenticatedUserEntity();
        String orderNo = orderService.placeOrder(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Order %s placed successfully".formatted(orderNo)));
    }

    @GetMapping("/{orderNo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Retrieve an order by order number",
            description = "Fetch details of a specific order using its order number."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g., missing or malformed order number)"),
            @ApiResponse(responseCode = "403", description = "Access denied (user lacks required roles)"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDto<Object>> getOrder(@PathVariable("orderNo") String orderNo){
        log.info("Inside getOrder method of OrderController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), orderService.getOrder(orderNo)));
    }
}
