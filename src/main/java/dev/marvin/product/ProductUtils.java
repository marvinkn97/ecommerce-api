package dev.marvin.product;

import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.shared.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductUtils {
    private final ProductRepository productRepository;

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND));
    }
}
