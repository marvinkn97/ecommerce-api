package dev.marvin.utils;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.Category;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryUtils {
    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
    }
}
