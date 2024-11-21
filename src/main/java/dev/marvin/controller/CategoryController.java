package dev.marvin.controller;

import dev.marvin.constants.MessageConstants;
import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.service.CategoryService;
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
@RequestMapping("api/v1/categories")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Category Resource", description = "CRUD Operations for Category Management")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Category", description = "Creates a new category. Requires ADMIN role.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Category added successfully"), @ApiResponse(responseCode = "409", description = "Duplicate entry"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryController");
        categoryService.add(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), MessageConstants.CATEGORY_CREATED));
    }

    @GetMapping
    @Operation(summary = "Fetch All Categories", description = "Retrieve a list of all categories available", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getAll() {
        log.info("Inside getAll method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), categoryService.getAll()));
    }

    @GetMapping("/paginated")
    @Operation(summary = "Fetch Paginated Categories", description = "Retrieve a paginated list of categories", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of categories"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getAllPaginated() {
        log.info("Inside getAllPaginated method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), categoryService.getAllPaginated()));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Fetch Category by ID", description = "Retrieve details of a specific category.", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved the specified category"), @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getOne(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside getOne method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), categoryService.getOne(categoryId)));
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a specific category", description = "Update a specific category. Requires ADMIN role.", method = "PUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data provided"), @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "403", description = "User does not have permission to update this category"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<String>> update(@PathVariable("categoryId") Integer categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryController");
        categoryService.update(categoryId, categoryRequest);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.CATEGORY_UPDATED));
    }

    @PutMapping("{categoryId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle Category Status for specific category", description = "Activate or deactivate an existing category. Requires ADMIN role.", method = "PUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category status updated successfully"), @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "403", description = "User does not have permission to modify this category"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<String>> toggleStatus(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside toggleStatus method of CategoryController");
        categoryService.toggleStatus(categoryId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.CATEGORY_STATUS_UPDATED));
    }
}
