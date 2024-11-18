package dev.marvin.controller;

import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Product Resource", description = "CRUD Operations for Product Management")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Product", description = "Creates a new product. Requires ADMIN role.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Product added successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error when adding product", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Inside add method of ProductController");
        log.info("request: {}", productRequest);
        return null;
        //return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(productRequest));
    }
}
