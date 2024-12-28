package dev.marvin.category;

import org.springframework.data.domain.Page;

import java.util.Collection;

public interface CategoryService {
    CategoryResponse add(CategoryRequest categoryRequest);

    Collection<CategoryResponse> getAll();

    Page<CategoryResponse> getAllPaginated();

    CategoryResponse getOne(Integer categoryId);

    CategoryResponse update(Integer categoryId, CategoryRequest categoryRequest);

    CategoryResponse toggleStatus(Integer categoryId);
}
