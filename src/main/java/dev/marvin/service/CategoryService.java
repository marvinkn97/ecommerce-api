package dev.marvin.service;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;

import java.util.Collection;

public interface CategoryService {
    void add(CategoryRequest categoryRequest);
    Collection<CategoryResponse> getAll();

}
