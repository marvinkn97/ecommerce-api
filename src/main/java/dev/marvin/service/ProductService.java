package dev.marvin.service;

import dev.marvin.dto.ProductRequest;
import dev.marvin.dto.ProductResponse;
import dev.marvin.dto.ResponseDto;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ProductService {
    ResponseDto<String> add(ProductRequest productRequest);

    Collection<ProductResponse> getAll();

    Page<ProductResponse> getAllPaginated();

    Page<ProductResponse> getAll(Integer categoryId);

    ProductResponse getOne(Integer productId);

    ProductResponse getOne(String productName);

    void update(Integer productId, ProductRequest productRequest);

    void delete(Integer productId);
}
