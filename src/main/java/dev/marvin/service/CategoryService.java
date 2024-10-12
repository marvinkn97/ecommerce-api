package dev.marvin.service;

import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.ResponseDto;

public interface CategoryService {
    ResponseDto<String> add(CategoryRequest categoryRequest);

    ResponseDto<Object> getAll();

    ResponseDto<Object> getAllPaginated();

    ResponseDto<Object> getOne(Integer categoryId);

    ResponseDto<String> update(Integer categoryId, CategoryRequest categoryRequest);

    ResponseDto<String> toggleStatus(Integer categoryId);

}
