package dev.marvin.service;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.dto.ResponseDto;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Map;

public interface CategoryService {
    ResponseDto<String> add(CategoryRequest categoryRequest);

    Collection<CategoryResponse> getAll();

    Page<CategoryResponse> getAllPaginated();

    CategoryResponse getOne(Integer categoryId);

    Map<String, Object> update(Integer categoryId, CategoryRequest categoryRequest);

    Map<String, Object> toggleStatus(Integer categoryId);

}
