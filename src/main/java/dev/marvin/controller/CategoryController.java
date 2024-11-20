package dev.marvin.controller;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Category added successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error when adding category", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("principal: {}", userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.add(categoryRequest));
    }

    @GetMapping
    @Operation(summary = "Fetch All Categories", description = "Retrieve a list of all categories available", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories"), @ApiResponse(responseCode = "500", description = "Internal server error occurred while fetching categories")})
    public ResponseEntity<ResponseDto<Object>> getAll() {
        log.info("Inside getAll method of CategoryController");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Fetch Paginated Categories", description = "Retrieve a paginated list of categories", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of categories"), @ApiResponse(responseCode = "500", description = "Internal server error occurred while fetching paginated categories")})
    public ResponseEntity<ResponseDto<Object>> getAllPaginated() {
        log.info("Inside getAllPaginated method of CategoryController");
        return ResponseEntity.ok(categoryService.getAllPaginated());
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Fetch Category by ID", description = "Retrieve details of a specific category.", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved the specified category"), @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "500", description = "Internal server error occurred while fetching the category")})
    public ResponseEntity<ResponseDto<Object>> getOne(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside getOne method of CategoryController");
        return ResponseEntity.ok(categoryService.getOne(categoryId));
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a specific category", description = "Update a specific category. Requires ADMIN role.", method = "PUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data provided"), @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "403", description = "User does not have permission to update this category"), @ApiResponse(responseCode = "500", description = "Internal server error occurred while updating category")})
    public ResponseEntity<ResponseDto<String>> update(@PathVariable("categoryId") Integer categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryController");
        return ResponseEntity.ok(categoryService.update(categoryId, categoryRequest));
    }

    @PutMapping("{categoryId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle Category Status for specific category", description = "Activate or deactivate an existing category. Requires ADMIN role.", method = "PUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category status updated successfully"), @ApiResponse(responseCode = "404", description = "Category not found for the provided ID"), @ApiResponse(responseCode = "403", description = "User does not have permission to modify this category"), @ApiResponse(responseCode = "500", description = "Internal server error occurred while updating category status")})
    public ResponseEntity<ResponseDto<String>> toggleStatus(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside toggleStatus method of CategoryController");
        return ResponseEntity.ok(categoryService.toggleStatus(categoryId));
    }
}
