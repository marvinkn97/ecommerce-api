package dev.marvin.category;

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
