package dev.marvin.service;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface CategoryService {
    void add(CategoryRequest categoryRequest);

    Collection<CategoryResponse> getAll();

    Page<CategoryResponse> getAllPaginated();

    CategoryResponse getOne(Integer categoryId);

    void update(Integer categoryId, CategoryRequest categoryRequest);

    void toggleStatus(Integer categoryId);

}
