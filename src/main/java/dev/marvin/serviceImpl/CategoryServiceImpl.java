package dev.marvin.serviceImpl;

import dev.marvin.domain.Category;
import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.repository.CategoryRepository;
import dev.marvin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void add(CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryServiceImpl");
        try {
            Category category = new Category();
            category.setCategoryName(categoryRequest.categoryName());
            category.setCreatedBy(1);
            category.setUpdatedBy(1);
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            throw new DuplicateResourceException("Category with given name already exists");
        } catch (Exception e) {
            log.error("Unexpected error occurred in add method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CategoryResponse> getAll() {
        log.info("Inside getAll method of CategoryServiceImpl");
        try {
            return categoryRepository.findAll().stream()
                    .map(category -> new CategoryResponse(category.getCategoryId(), category.getCategoryName(), category.getCreatedAt()))
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAll method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }


}
