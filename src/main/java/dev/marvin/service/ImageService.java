package dev.marvin.service;

import dev.marvin.domain.Image;
import dev.marvin.domain.Product;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.ImageRepository;
import dev.marvin.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final ProductUtils productUtils;

    @Override
    public Image getImageById(Integer id) {
        log.info("Inside getImageById of ImageService");
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("image not found"));
    }

    @Override
    public void deleteImage(Integer id) {
        log.info("Inside deleteImage of ImageService");
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () ->
        {
            throw new ResourceNotFoundException("image not found");
        });

    }

    @Override
    public void saveImage(Collection<MultipartFile> files, Integer productId) {
        log.info("Inside saveImage of ImageService");
        Product product = productUtils.getProductById(productId);
        try {
            for (MultipartFile file : files) {
                Image image = new Image();
                image.setProduct(product);
                image.setFileName(file.getOriginalFilename());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setFileType(file.getContentType());

                Image savedImage = imageRepository.save(image);
                String downloadUrl = "api/v1/images/image/download/%s".formatted(savedImage.getId());
                image.setDownloadUrl(downloadUrl);
                imageRepository.save(savedImage);
            }

        } catch (IOException | SQLException e) {
            throw new RequestValidationException("unable to save image", e);
        }

    }

    @Override
    public void updateImage(MultipartFile file, Integer imageId) {
        log.info("Inside updateImage of ImageService");
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RequestValidationException("unable to save image", e);
        }

    }
}
