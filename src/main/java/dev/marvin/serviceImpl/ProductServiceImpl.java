package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.constants.PaginationConstants;
import dev.marvin.domain.Category;
import dev.marvin.domain.Product;
import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ProductResponse;
import dev.marvin.dto.ProductUpdateRequest;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.repository.ProductRepository;
import dev.marvin.service.ProductService;
import dev.marvin.utils.CategoryUtils;
import dev.marvin.utils.Mapper;
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
            Product product = new Product();
            product.setProductName(productRequest.productName());
            product.setProductPrice(productRequest.productPrice());
            product.setDiscountPrice(productRequest.discountPrice());
            product.setProductDescription(productRequest.productDescription());
            product.setProductQuantity(productRequest.productQuantity());
            product.setIsDeleted(Boolean.FALSE);

            Category category = categoryUtils.getCategoryById(productRequest.categoryId());
            product.setCategory(category);
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
            log.info("Updating product description from '{}' to '{}'", product.getProductDescription(), updatedDescription);
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
        Boolean status = product.getIsDeleted();
        Boolean updatedStatus = status.equals(Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE;
        product.setIsDeleted(updatedStatus);
        productRepository.save(product);
        log.info("Product with ID [{}] successfully updated", productId);
    }

}

