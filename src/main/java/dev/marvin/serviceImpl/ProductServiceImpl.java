package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.constants.PaginationConstants;
import dev.marvin.domain.Category;
import dev.marvin.domain.Product;
import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ProductResponse;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.ServiceException;
import dev.marvin.mapper.Mapper;
import dev.marvin.repository.CategoryRepository;
import dev.marvin.repository.ProductRepository;
import dev.marvin.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void add(ProductRequest productRequest) {
        log.info("Inside add method of ProductServiceImpl");
        try {
            if (!ObjectUtils.isEmpty(productRequest)) {
                Product product = new Product();
                product.setProductId(UUID.randomUUID().toString());
                product.setProductName(productRequest.productName());
                product.setProductPrice(product.getProductPrice());
                product.setProductDescription(product.getProductDescription());

                //TODO: find a way to store this product image in cloud storage
                byte[] imageBytes = productRequest.encodedProductImage().getBytes(StandardCharsets.UTF_8);
                product.setImageBytes(imageBytes);

                Category category = categoryRepository.findById(productRequest.categoryId())
                        .orElse(null);
                product.setCategory(category);
                productRepository.save(product);
            }
        } catch (DataIntegrityViolationException ex) {
            log.error("DataIntegrityViolationException {}", ex.getMessage(), ex);
            if (ex.getMessage().contains("product_name")) {
                throw new DuplicateResourceException(MessageConstants.DUPLICATE_PRODUCT_NAME);
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred in add method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    @Override
    public Page<ProductResponse> getAll() {
        log.info("Inside getAll method of ProductServiceImpl");
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, PaginationConstants.SORT_COLUMN);
            Pageable pageable = PageRequest.of(PaginationConstants.PAGE_NUMBER, PaginationConstants.PAGE_SIZE, sort);
            Page<Product> productPage = productRepository.getProducts(pageable);
            List<ProductResponse> productResponseList = productPage.getContent().stream().map(Mapper::mapToDto).toList();
            return new PageImpl<>(productResponseList, pageable, productPage.getTotalElements());
        } catch (Exception e) {
            log.error("Unexpected error occurred in getAll method of ProductServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public ProductResponse getOne(Integer productId) {
        return null;
    }

    @Override
    public ProductResponse getOne(String productName) {
        return null;
    }

    @Override
    public void update(Integer productId, ProductRequest productRequest) {

    }

    @Override
    public void delete(Integer productId) {

    }

}

