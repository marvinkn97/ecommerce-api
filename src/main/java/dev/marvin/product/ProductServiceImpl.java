package dev.marvin.service;

import dev.marvin.shared.MessageConstants;
import dev.marvin.shared.PaginationConstants;
import dev.marvin.category.Category;
import dev.marvin.product.Product;
import dev.marvin.product.ProductRequest;
import dev.marvin.product.ProductResponse;
import dev.marvin.product.ProductUpdateRequest;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.product.ProductRepository;
import dev.marvin.category.CategoryUtils;
import dev.marvin.shared.Mapper;
import dev.marvin.shared.Status;
import dev.marvin.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
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
    private final CategoryUtils categoryUtils;
    private final ProductUtils productUtils;

    @Override
    public void add(ProductRequest productRequest) {
        log.info("Inside add method of ProductServiceImpl");
        try {
            Category category = categoryUtils.getCategoryById(productRequest.categoryId());
            Product product = Product.builder()
                    .name(productRequest.productName())
                    .price(productRequest.productPrice())
                    .discountPrice(productRequest.discountPrice())
                    .description(productRequest.productDescription())
                    .quantity(productRequest.productQuantity())
                    .category(category)
                    .build();
            productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            if (ex.getMessage().contains("product_name")) {
                throw new DuplicateResourceException(MessageConstants.DUPLICATE_PRODUCT_NAME);
            }
        }
    }

    @Override
    public Collection<ProductResponse> getAll() {
        log.info("Inside getAll method of ProductServiceImpl");
        return productRepository.getProducts().stream().map(Mapper::mapToDto).toList();
    }

    @Override
    public Page<ProductResponse> getAllPaginated() {
        log.info("Inside getAllPaginated method of ProductServiceImpl");
        Sort sort = Sort.by(Sort.Direction.DESC, PaginationConstants.PRODUCT_SORT_COLUMN);
        Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, sort);
        Page<Product> productPage = productRepository.getProducts(pageable);
        List<ProductResponse> productResponseList = productPage.getContent().stream().map(Mapper::mapToDto).toList();
        return new PageImpl<>(productResponseList, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductResponse> getAllByCategory(Integer categoryId) {
        log.info("Inside getAllByCategory method of ProductServiceImpl");
        Sort sort = Sort.by(Sort.Direction.DESC, PaginationConstants.PRODUCT_SORT_COLUMN);
        Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, sort);
        Page<Product> productPage = productRepository.getProductsByCategory(categoryId, pageable);
        List<ProductResponse> products = productPage.getContent().stream().map(Mapper::mapToDto).toList();
        return new PageImpl<>(products, pageable, productPage.getTotalElements());
    }

    @Override
    public ProductResponse getOne(Integer productId) {
        log.info("Inside getOne method of ProductServiceImpl");
        Product product = productUtils.getProductById(productId);
        return Mapper.mapToDto(product);
    }


    @Override
    public void update(Integer productId, ProductUpdateRequest productUpdateRequest) {
        log.info("Inside update method of ProductServiceImpl");
        Product product = productUtils.getProductById(productId);
        boolean changes = false;

        String updatedName = productUpdateRequest.productName();
        if (StringUtils.hasText(updatedName) && !updatedName.equals(product.getName())) {
            log.info("Updating product name from '{}' to '{}'", product.getName(), updatedName);
            product.setName(updatedName);
            changes = true;
        }

        BigDecimal updatedPrice = productUpdateRequest.productPrice();
        if (updatedPrice != null && !updatedPrice.equals(product.getPrice())) {
            log.info("Updating product price from '{}' to '{}'", product.getPrice(), updatedPrice);
            product.setPrice(updatedPrice);
            changes = true;
        }

        BigDecimal updatedDiscount = productUpdateRequest.discountPrice();
        if (updatedDiscount != null && !updatedDiscount.equals(product.getDiscountPrice())) {
            log.info("Updating discount price from '{}' to '{}'", product.getDiscountPrice(), updatedDiscount);
            product.setDiscountPrice(updatedDiscount);
            changes = true;
        }

        String updatedDescription = productUpdateRequest.productDescription();
        if (StringUtils.hasText(updatedDescription) && !updatedDescription.equals(product.getDescription())) {
            log.info("Updating product description from '{}' to '{}'", product.getDescription(), updatedDescription);
            product.setDescription(updatedDescription);
            changes = true;
        }

        Integer updatedQuantity = productUpdateRequest.productQuantity();
        if (updatedQuantity != null && !updatedQuantity.equals(product.getQuantity())) {
            log.info("Updating product quantity from '{}' to '{}'", product.getQuantity(), updatedQuantity);
            product.setQuantity(updatedQuantity);
            changes = true;
        }

        if (!changes) {
            log.warn("No updates were provided");
            throw new RequestValidationException("No changes detected in the request");
        }

        productRepository.save(product);
        log.info("Product with ID [{}] successfully updated", productId);
    }

    @Override
    public void toggleStatus(Integer productId) {
        log.info("Inside toggleStatus method of ProductServiceImpl");
        Product product = productUtils.getProductById(productId);
        Status currentStatus = product.getStatus();
        Status updatedStatus = currentStatus.equals(Status.ACTIVE) ? Status.INACTIVE : Status.ACTIVE;
        product.setStatus(updatedStatus);
        productRepository.save(product);
        log.info("Product with ID [{}] successfully updated", productId);
    }

}

