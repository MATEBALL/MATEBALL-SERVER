package at.mateball.storage;

import at.mateball.storage.dto.ImageUploadRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {

    ImageUploadRes uploadProfileImage(MultipartFile file) throws IOException;

    String getImageUrl(String objectKey);

    void deleteObject(String objectKey);
}
