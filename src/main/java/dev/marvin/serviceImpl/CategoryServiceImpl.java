package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.constants.PaginationConstants;
import dev.marvin.domain.Category;
import dev.marvin.domain.Status;
import dev.marvin.dto.CategoryRequest;
import dev.marvin.dto.CategoryResponse;
import dev.marvin.dto.ResponseDto;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.exception.ServiceException;
import dev.marvin.mapper.Mapper;
import dev.marvin.repository.CategoryRepository;
import dev.marvin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ResponseDto<String> add(CategoryRequest categoryRequest) {
        log.info("Inside add method of CategoryServiceImpl");
        try {
            Category category = new Category();
            category.setCategoryName(categoryRequest.categoryName());
            category.setStatus(Status.Active);
            Category savedCategory = categoryRepository.save(category);
            return new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Category added successfully with Id %s".formatted(savedCategory.getId()));
        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            if (ex.getMessage().contains("category_name")) {
                throw new DuplicateResourceException(MessageConstants.DUPLICATE_CATEGORY_NAME);
            } else {
                throw new DuplicateResourceException(ex.getMessage());
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred in add method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CategoryResponse> getAll() {
        log.info("Inside getAll method of CategoryServiceImpl");
        try {
            return categoryRepository.findAll().stream().map(Mapper::mapToDto)
                    .toList();
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAllPaginated method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllPaginated() {
        log.info("Inside getAllPaginated method of CategoryServiceImpl");
        try {
            Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, Sort.by(Sort.Direction.DESC, PaginationConstants.SORT_COLUMN));
            Page<Category> categoryPage = categoryRepository.getCategories(pageable);
            List<CategoryResponse> categoryResponseList = categoryPage.getContent().stream().map(Mapper::mapToDto)
                    .toList();
            return new PageImpl<>(categoryResponseList, pageable, categoryPage.getTotalElements());
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAllPaginated method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getOne(Integer categoryId) {
        log.info("Inside getOne method of CategoryServiceImpl");
        try {
            return categoryRepository.findById(categoryId)
                    .map(Mapper::mapToDto)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
        } catch (ResourceNotFoundException e) {
            log.error("Category not found exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in getOne method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    @Override
    @Transactional
    public Map<String, Object> update(Integer categoryId, CategoryRequest categoryRequest) {
        log.info("Inside update method of CategoryServiceImpl");
        try {
            Category category = getCategoryById(categoryId);
            if (StringUtils.hasText(categoryRequest.categoryName())) {
                category.setCategoryName(categoryRequest.categoryName());
            }
            categoryRepository.save(category);
            return Map.of("status", HttpStatus.OK.getReasonPhrase(), "message", "Category with Id %s updated successfully".formatted(category.getId()));

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
    public Map<String, Object> toggleStatus(Integer categoryId) {
        log.info("Inside toggleStatus method of CategoryServiceImpl");
        try {
            Category category = getCategoryById(categoryId);
            Status currentStatus = category.getStatus();
            Status updatedStatus = currentStatus.equals(Status.Active) ? Status.Inactive : Status.Active;
            category.setStatus(updatedStatus);
            categoryRepository.save(category);
            return Map.of("status", HttpStatus.OK.getReasonPhrase(), "message", "Category status with Id %s updated successfully".formatted(category.getId()));

        } catch (ResourceNotFoundException e) {
            log.error("Category not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in toggleStatus method of CategoryServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
    }

}
