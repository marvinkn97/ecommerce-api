package dev.marvin.controller;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.dto.ProductRequest;
import dev.marvin.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Product Resource", description = "CRUD Operations for Product Management")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "create", description = "create a new product", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Product added successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to add Product")})
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> add(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Inside add method of ProductController");
        productService.add(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    @GetMapping
    @Operation(summary = "fetch all", description = "retrieve a list of products", method = "GET")
    public ResponseEntity<Page<CategoryResponse>> getAll() {
        log.info("Inside getAll method of CategoryController");
//        return ResponseEntity.ok(productService.getAll());
        return null;
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "fetch one", description = "retrieve a specified category", method = "GET")
    public ResponseEntity<CategoryResponse> getOne(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside getOne method of CategoryController");
//        return ResponseEntity.ok(productService.getOne(categoryId));
        return null;
    }


    @PutMapping("/{categoryId}")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update", description = "update an existing category", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to update Category")})
    public ResponseEntity<String> update(@PathVariable("categoryId") Integer categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryController");
//        categoryService.update(categoryId, categoryRequest);
//        return ResponseEntity.ok("Category updated successfully");
        return null;
    }

    @DeleteMapping("/{categoryId}")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete", description = "delete an existing category", method = "DELETE")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to delete Category")})
    public ResponseEntity<String> delete(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside delete method of CategoryController");
//        categoryService.delete(categoryId);
//        return ResponseEntity.ok("Category deleted successfully");
        return null;
    }

    @PutMapping("/{productId}/upload")
    public ResponseEntity<String> uploadProductImage(@PathVariable("productId") Integer productId, @RequestParam("image")MultipartFile multipartFile){
        return null;
    }
}
