package dev.marvin.service;

import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ProductResponse;
import dev.marvin.dto.ProductUpdateRequest;
import dev.marvin.dto.ResponseDto;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ProductService {
    ResponseDto<String> add(ProductRequest productRequest);

    Collection<ProductResponse> getAll();

    Page<ProductResponse> getAllPaginated();

    Page<ProductResponse> getAllByCategory(Integer categoryId);

    ProductResponse getOne(Integer productId);

    void update(Integer productId, ProductUpdateRequest updateRequest);

    void toggleStatus(Integer productId);
}
