package dev.marvin.product;

import dev.marvin.shared.MessageConstants;
import dev.marvin.shared.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Product Resource", description = "CRUD Operations for Product Management")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Product", description = "Creates a new product. Requires ADMIN role.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Product added successfully"), @ApiResponse(responseCode = "400", description = "Invalid request"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Inside add method of ProductController");
        productService.add(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), MessageConstants.PRODUCT_CREATED));
    }

    @GetMapping
    @Operation(summary = "Fetch All Products", description = "Retrieve a list of all products available", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getAll() {
        log.info("Inside getAll method of ProductControllerController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), productService.getAll()));
    }

    @GetMapping("/paginated")
    @Operation(summary = "Fetch Paginated products", description = "Retrieve a paginated list of products", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of products"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getAllPaginated() {
        log.info("Inside getAllPaginated method of ProductController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), productService.getAllPaginated()));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Fetch Product by ID", description = "Retrieve details of a specific product.", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved the specified product"), @ApiResponse(responseCode = "404", description = "Product not found for the provided ID"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getOne(@PathVariable("productId") Integer productId) {
        log.info("Inside getOne method of ProductController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), productService.getOne(productId)));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Product Details", description = "Partially update a product by providing one or more fields", method = "PUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Product updated successfully"), @ApiResponse(responseCode = "404", description = "Product not found"), @ApiResponse(responseCode = "400", description = "Invalid input provided"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<String>> update(@PathVariable Integer productId, @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        log.info("Inside update method of ProductController");
        productService.update(productId, productUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.PRODUCT_UPDATED));
    }

    @PutMapping("{productId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle Visibility Status for specific product", description = "Activate or deactivate an existing product. Requires ADMIN role.", method = "PUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Product visibility status updated successfully"), @ApiResponse(responseCode = "404", description = "Product not found"), @ApiResponse(responseCode = "403", description = "User does not have permission to modify this product"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<String>> toggleStatus(@PathVariable("productId") Integer productId) {
        log.info("Inside toggleStatus method of ProductController");
        productService.toggleStatus(productId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.PRODUCT_STATUS_UPDATED));
    }

}
