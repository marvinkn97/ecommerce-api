package dev.marvin.image;

import dev.marvin.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface ImageService {
    Image getImageById(Integer id);
    void deleteImage(Integer id);
    void saveImage(Collection<MultipartFile> files, Integer productId);
    void updateImage(MultipartFile file, Integer imageId);
}
