package dev.marvin.utils;

import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.product.Product;
import dev.marvin.product.ProductRepository;
import dev.marvin.shared.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductUtils {
    private final ProductRepository productRepository;

    public Product getProductById(Integer productId) {
        return productRepository.getProductById(productId).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND));
    }
}
