package dev.marvin.category;

import dev.marvin.constants.MessageConstants;
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
@RequestMapping("${app.api.prefix}/categories")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Category Resource", description = "CRUD Operations for Category Management")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create Category", description = "Creates a new category. Requires ADMIN role.", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category added successfully"),
            @ApiResponse(responseCode = "409", description = "Duplicate entry"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<CategoryResponse>> add(@Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryController");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(MessageConstants.CATEGORY_CREATED, categoryService.add(categoryRequest)));
    }

    @GetMapping
    @Operation(summary = "Fetch All Categories", description = "Retrieve a list of all categories available", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getAll() {
        log.info("Inside getAll method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(null, categoryService.getAll()));
    }

    @GetMapping("/paginated")
    @Operation(summary = "Fetch Paginated Categories", description = "Retrieve a paginated list of categories", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of categories"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getAllPaginated() {
        log.info("Inside getAllPaginated method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(null, categoryService.getAllPaginated()));
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Fetch Category by ID", description = "Retrieve details of a specific category.", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the specified category"),
            @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> getOne(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside getOne method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(null, categoryService.getOne(categoryId)));
    }

    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a specific category", description = "Update a specific category. Requires ADMIN role.", method = "PATCH")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided"),
            @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to update this category"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<CategoryResponse>> update(@PathVariable("categoryId") Integer categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(MessageConstants.CATEGORY_UPDATED, categoryService.update(categoryId, categoryRequest)));
    }

    @PatchMapping("{categoryId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle Category Status for specific category", description = "Activate or deactivate an existing category. Requires ADMIN role.", method = "PATCH")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to modify this category"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<CategoryResponse>> toggleStatus(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside toggleStatus method of CategoryController");
        return ResponseEntity.ok(new ResponseDto<>(MessageConstants.CATEGORY_STATUS_UPDATED, categoryService.toggleStatus(categoryId)));
    }
}
