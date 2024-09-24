package dev.marvin.mapper;

import dev.marvin.domain.Category;
import dev.marvin.dto.CategoryResponse;

public class CategoryMapper {
    private CategoryMapper(){}
    public static CategoryResponse mapToDto(Category category){
        return new CategoryResponse(category.getId(), category.getCategoryName(), category.getCreatedAt());
    }
}
