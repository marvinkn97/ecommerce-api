package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.constants.PaginationConstants;
import dev.marvin.domain.Category;
import dev.marvin.domain.Product;
import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ProductResponse;
import dev.marvin.dto.ProductUpdateRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.exception.ServiceException;
import dev.marvin.utils.Mapper;
import dev.marvin.repository.CategoryRepository;
import dev.marvin.repository.ProductRepository;
import dev.marvin.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseDto<String> add(ProductRequest productRequest) {
        log.info("Inside add method of ProductServiceImpl");
        try {
            Product product = new Product();
            product.setProductName(productRequest.productName());
            product.setProductPrice(productRequest.productPrice());
            product.setDiscountPrice(productRequest.discountPrice());
            product.setProductDescription(productRequest.productDescription());
            product.setProductQuantity(productRequest.productQuantity());
            product.setIsDeleted(Boolean.FALSE);

            Category category = categoryRepository.findById(productRequest.categoryId()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.CATEGORY_NOT_FOUND));
            product.setCategory(category);

            Product savedProduct = productRepository.save(product);
            return new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Product added successfully with Id %s".formatted(savedProduct.getId()));

        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            if (ex.getMessage().contains("product_name")) {
                throw new DuplicateResourceException(MessageConstants.DUPLICATE_PRODUCT_NAME);
            } else {
                throw new DuplicateResourceException(ex.getMessage());
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred in add method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    @Override
    public Collection<ProductResponse> getAll() {
        log.info("Inside getAll method of ProductServiceImpl");
        try {
            return productRepository.getProducts().stream().map(Mapper::mapToDto).toList();
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAllPaginated method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public Page<ProductResponse> getAllPaginated() {
        log.info("Inside getAllPaginated method of ProductServiceImpl");
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, PaginationConstants.PRODUCT_SORT_COLUMN);
            Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, sort);
            Page<Product> productPage = productRepository.getProducts(pageable);
            List<ProductResponse> productResponseList = productPage.getContent().stream().map(Mapper::mapToDto).toList();
            return new PageImpl<>(productResponseList, pageable, productPage.getTotalElements());
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAllPaginated method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public Page<ProductResponse> getAllByCategory(Integer categoryId) {
        log.info("Inside getAllByCategory method of ProductServiceImpl");
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, PaginationConstants.PRODUCT_SORT_COLUMN);
            Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, sort);
            Page<Product> productPage = productRepository.getProductsByCategory(categoryId, pageable);
            List<ProductResponse> products = productPage.getContent().stream().map(Mapper::mapToDto).toList();
            return new PageImpl<>(products, pageable, productPage.getTotalElements());
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAllByCategory method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public ProductResponse getOne(Integer productId) {
        log.info("Inside getOne method of ProductServiceImpl");
        try {
            Product product = productRepository.getProductById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id [%s] not found".formatted(productId)));
            return Mapper.mapToDto(product);
        } catch (ResourceNotFoundException e) {
            log.error("Product not found exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in getOne method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }


    @Override
    public void update(Integer productId, ProductUpdateRequest productUpdateRequest) {
        log.info("Inside update method of ProductServiceImpl");
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with id [%d] not found".formatted(productId)));
            boolean changes = false;

            String updatedName = productUpdateRequest.productName();
            if (StringUtils.hasText(updatedName) && !updatedName.equals(product.getProductName())) {
                log.info("Updating product name from '{}' to '{}'", product.getProductName(), updatedName);
                product.setProductName(updatedName);
                changes = true;
            }

            BigDecimal updatedPrice = productUpdateRequest.productPrice();
            if (updatedPrice != null && !updatedPrice.equals(product.getProductPrice())) {
                log.info("Updating product price from '{}' to '{}'", product.getProductPrice(), updatedPrice);
                product.setProductPrice(updatedPrice);
                changes = true;
            }

            BigDecimal updatedDiscount = productUpdateRequest.discountPrice();
            if (updatedDiscount != null && !updatedDiscount.equals(product.getDiscountPrice())) {
                log.info("Updating discount price from '{}' to '{}'", product.getDiscountPrice(), updatedDiscount);
                product.setDiscountPrice(updatedDiscount);
                changes = true;
            }

            String updatedDescription = productUpdateRequest.productDescription();
            if (StringUtils.hasText(updatedDescription) && !updatedDescription.equals(product.getProductDescription())) {
                log.info("Updating product description");
                product.setProductDescription(updatedDescription);
                changes = true;
            }

            Integer updatedQuantity = productUpdateRequest.productQuantity();
            if (updatedQuantity != null && !updatedQuantity.equals(product.getProductQuantity())) {
                log.info("Updating product quantity from '{}' to '{}'", product.getProductQuantity(), updatedQuantity);
                product.setProductQuantity(updatedQuantity);
                changes = true;
            }

            if (!changes) {
                log.warn("No updates were provided for Product ID: {}", productId);
                throw new RequestValidationException("No changes detected in the request");
            }

            productRepository.save(product);
            log.info("Product with ID [{}] successfully updated", productId);

        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            throw e;
        } catch (RequestValidationException e) {
            log.error("Validation error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in update method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public void toggleStatus(Integer productId) {
        log.info("Inside toggleStatus method of ProductServiceImpl");
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with id [%d] not found".formatted(productId)));
            Boolean status = product.getIsDeleted();
            Boolean updatedStatus = status.equals(Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE;
            product.setIsDeleted(updatedStatus);
            productRepository.save(product);
            log.info("Product with ID [{}] successfully updated", productId);

        } catch (ResourceNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in toggleStatus method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

}

