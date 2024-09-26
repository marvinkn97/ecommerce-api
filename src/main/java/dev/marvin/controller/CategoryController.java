package dev.marvin.controller;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Category Resource", description = "CRUD Operations for Category Management")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @Operation(summary = "create", description = "create a new category", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Category added successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to add Category")})
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> add(@RequestBody CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryController");
        categoryService.add(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category added successfully");
    }

    @GetMapping("/public/categories")
    //retrieve a list of category - swagger def
    public ResponseEntity<Page<CategoryResponse>> getAll() {
        log.info("Inside getAll method of CategoryController");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/public/categories/{categoryId}")
    //retrieve existing category - swagger def
    public ResponseEntity<Object> getOne(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside getOne method of CategoryController");
        return ResponseEntity.ok( categoryService.getOne(categoryId));
    }


    @PutMapping("/admin/categories/{categoryId}")
    //@PreAuthorize("hasRole('ADMIN')")
    //update an existing category - swagger def
    public void update(@PathVariable("categoryId") Integer categoryId, @RequestBody CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryController");
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    //@PreAuthorize("hasRole('ADMIN')")
    //delete an existing category - swagger def
    public void delete(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside delete method of CategoryController");
    }

}
