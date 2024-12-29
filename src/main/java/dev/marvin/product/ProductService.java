package dev.marvin.product;

import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ProductService {
    ProductResponse add(ProductRequest productRequest);

    Collection<ProductResponse> getAll();

    Page<ProductResponse> getAllPaginated();

    Page<ProductResponse> getAllByCategory(Integer categoryId);

    ProductResponse getOne(Integer productId);

    ProductResponse update(Integer productId, ProductUpdateRequest updateRequest);

    ProductResponse toggleStatus(Integer productId);
}
