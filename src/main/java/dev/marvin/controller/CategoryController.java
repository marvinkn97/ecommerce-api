package dev.marvin.controller;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/categories")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Category Resource", description = "CRUD Operations for Category Management")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "create", description = "create a new category", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Category added successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to add Category")})
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> add(@Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryController");
        categoryService.add(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category added successfully");
    }

    @GetMapping
    @Operation(summary = "fetch all", description = "retrieve a list of categories", method = "GET")
    public ResponseEntity<Collection<CategoryResponse>> getAll() {
        log.info("Inside getAll method of CategoryController");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/paginated")
    @Operation(summary = "fetch all paginated", description = "retrieve a paginated list of categories", method = "GET")
    public ResponseEntity<Page<CategoryResponse>> getAllPaginated() {
        log.info("Inside getAllPaginated method of CategoryController");
        return ResponseEntity.ok(categoryService.getAllPaginated());
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "fetch one", description = "retrieve a specified category", method = "GET")
    public ResponseEntity<CategoryResponse> getOne(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside getOne method of CategoryController");
        return ResponseEntity.ok(categoryService.getOne(categoryId));
    }


    @PutMapping("/{categoryId}")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update", description = "update an existing category", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to update Category")})
    public ResponseEntity<String> update(@PathVariable("categoryId") Integer categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryController");
        categoryService.update(categoryId, categoryRequest);
        return ResponseEntity.ok("Category updated successfully");
    }

    @PutMapping("{categoryId}/toggle-status")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "toggle status", description = "activate/deactivate existing category", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category status updated successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to update Category status")})
    public ResponseEntity<String> toggleStatus(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside delete method of CategoryController");
        categoryService.toggleStatus(categoryId);
        return ResponseEntity.ok("Category status updated successfully");
    }

}
