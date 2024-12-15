package dev.marvin.service;

import dev.marvin.domain.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface IImageService {
    Image getImageById(Integer id);
    void deleteImage(Integer id);
    void saveImage(Collection<MultipartFile> files, Integer productId);
    void updateImage(MultipartFile file, Integer imageId);
}
