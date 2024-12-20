package dev.marvin.category;

import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.shared.MessageConstants;
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
