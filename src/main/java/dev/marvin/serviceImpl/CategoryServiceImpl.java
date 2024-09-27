package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.constants.PaginationConstants;
import dev.marvin.domain.Category;
import dev.marvin.domain.Status;
import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.exception.ServiceException;
import dev.marvin.mapper.CategoryMapper;
import dev.marvin.repository.CategoryRepository;
import dev.marvin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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
            category.setStatus(Status.Active);
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            throw new DuplicateResourceException(MessageConstants.DUPLICATE_CATEGORY_NAME);
        } catch (Exception e) {
            log.error("Unexpected error occurred in add method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAll() {
        log.info("Inside getAll method of CategoryServiceImpl");
        try {
            Pageable pageable = PageRequest.of(PaginationConstants.pageNumber, PaginationConstants.pageSize, Sort.by(Sort.Direction.DESC, PaginationConstants.sortColumn));
            Page<Category> categoryPage = categoryRepository.getCategories(pageable);
            List<CategoryResponse> categoryResponseList = categoryPage.getContent().stream().map(CategoryMapper::mapToDto)
                    .toList();
            return new PageImpl<>(categoryResponseList, pageable, categoryPage.getTotalElements());
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAll method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getOne(Integer categoryId) {
        log.info("Inside getOne method of CategoryServiceImpl");
        return categoryRepository.findById(categoryId)
                .map(CategoryMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void update(Integer categoryId, CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryServiceImpl");
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
            if (StringUtils.hasText(categoryRequest.categoryName())) {
                category.setCategoryName(categoryRequest.categoryName());
            }
            categoryRepository.save(category);
        } catch (ResourceNotFoundException e) {
            log.error("Category not found exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in update method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    @Override
    @Transactional
    public void delete(Integer categoryId) {
        log.info("Inside delete method of CategoryServiceImpl");
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
            category.setStatus(Status.Inactive);
            categoryRepository.save(category);
        } catch (ResourceNotFoundException e) {
            log.error("Category not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in update method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }


}
