package dev.marvin.service;

import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    void add(ProductRequest productRequest);

    Page<ProductResponse> getAll();

    Page<ProductResponse> getAll(Integer categoryId);

    ProductResponse getOne(Integer productId);

    ProductResponse getOne(String productName);

    void update(Integer productId, ProductRequest productRequest);

    void delete(Integer productId);
}
