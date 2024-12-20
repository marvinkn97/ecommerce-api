package dev.marvin.product;

import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ProductService {
    void add(ProductRequest productRequest);

    Collection<ProductResponse> getAll();

    Page<ProductResponse> getAllPaginated();

    Page<ProductResponse> getAllByCategory(Integer categoryId);

    ProductResponse getOne(Integer productId);

    void update(Integer productId, ProductUpdateRequest updateRequest);

    void toggleStatus(Integer productId);
}
