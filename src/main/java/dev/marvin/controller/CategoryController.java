package dev.marvin.controller;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("api/v1/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody CategoryRequest[] categoryRequest) {
        log.info("Inside add method of CategoryController");
        Arrays.stream(categoryRequest).forEach(categoryService::add);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        log.info("Inside getAll method of CategoryController");
        return ResponseEntity.ok(categoryService.getAll());
    }
}
