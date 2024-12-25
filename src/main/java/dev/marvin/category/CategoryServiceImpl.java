package dev.marvin.category;

import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.shared.Mapper;
import dev.marvin.shared.MessageConstants;
import dev.marvin.shared.PaginationConstants;
import dev.marvin.shared.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryUtils categoryUtils;

    @Override
    @Transactional
    public void add(CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryServiceImpl");
        try {
            Category category = Category.builder()
                    .name(categoryRequest.categoryName())
                    .build();
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            if (ex.getMessage().contains("category_name")) {
                throw new DuplicateResourceException(MessageConstants.DUPLICATE_CATEGORY_NAME);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CategoryResponse> getAll() {
        log.info("Inside getAll method of CategoryServiceImpl");
        return categoryRepository.findAll().stream().map(Mapper::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllPaginated() {
        log.info("Inside getAllPaginated method of CategoryServiceImpl");
        Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC, PaginationConstants.CATEGORY_SORT_COLUMN));
        Page<Category> categoryPage = categoryRepository.getCategories(pageable);
        List<CategoryResponse> categoryResponseList = categoryPage.getContent().stream().map(Mapper::mapToDto).toList();
        return new PageImpl<>(categoryResponseList, pageable, categoryPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getOne(Integer categoryId) {
        log.info("Inside getOne method of CategoryServiceImpl");
        return categoryRepository.findById(categoryId).map(Mapper::mapToDto).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void update(Integer categoryId, CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryServiceImpl");
        boolean changes = false;
        Category category = categoryUtils.getCategoryById(categoryId);
        if (StringUtils.hasText(categoryRequest.categoryName()) && !category.getName().equals(categoryRequest.categoryName())) {
            category.setName(categoryRequest.categoryName());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void toggleStatus(Integer categoryId) {
        log.info("Inside toggleStatus method of CategoryServiceImpl");
        Category category = categoryUtils.getCategoryById(categoryId);
        Status currentStatus = category.getStatus();
        Status updatedStatus = currentStatus.equals(Status.ACTIVE) ? Status.INACTIVE : Status.ACTIVE;
        category.setStatus(updatedStatus);
        categoryRepository.save(category);
    }
}
