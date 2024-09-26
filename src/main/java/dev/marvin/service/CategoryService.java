package dev.marvin.service;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    void add(CategoryRequest categoryRequest);

    Page<CategoryResponse> getAll();

    CategoryResponse getOne(Integer categoryId);

    void update(Integer categoryId, CategoryRequest categoryRequest);

    void delete(Integer categoryId);

}
