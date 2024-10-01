package dev.marvin.controller;

import dev.marvin.dto.ProductRequest;
import dev.marvin.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Product Resource", description = "CRUD Operations for Product Management")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "create", description = "create a new product", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Product added successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to add Product")})
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> add(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Inside add method of ProductController");
        productService.add(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }
}
