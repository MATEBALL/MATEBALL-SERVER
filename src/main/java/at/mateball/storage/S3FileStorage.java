package at.mateball.storage;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.storage.dto.ImageUploadRes;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileStorage implements FileStorage {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final S3Properties s3Properties;
    private final AmazonS3 amazonS3;

    @Override
    public ImageUploadRes uploadProfileImage(MultipartFile file) throws IOException {
        validateImage(file);

        String originalFileName = Objects.requireNonNull(file.getOriginalFilename(), "파일명이 없습니다.");
        String contentType = Objects.requireNonNull(file.getContentType(), "콘텐츠 타입이 없습니다.");

        String objectKey = createObjectKey("profiles", originalFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(contentType);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(s3Properties.getBucket(), objectKey, file.getInputStream(), metadata);

        amazonS3.putObject(putObjectRequest);

        return new ImageUploadRes(
                objectKey,
                originalFileName,
                contentType
        );
    }

    @Override
    public String getImageUrl(String objectKey) {
        String key = (objectKey == null || objectKey.isBlank())
                ? s3Properties.getDefaultProfileKey()
                : objectKey;

        return amazonS3.getUrl(s3Properties.getBucket(), key).toString();
    }

    @Override
    public void deleteObject(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return;
        }

        amazonS3.deleteObject(s3Properties.getBucket(), objectKey);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.EMPTY_PROFILE_IMAGE);
        }

        validateContentType(file);
        validateFileSize(file);
    }

    private String createObjectKey(String dirName, String originalFileName) {
        String safeFileName = originalFileName.replaceAll("\\s+", "_");
        return dirName + "/" + UUID.randomUUID() + "_" + safeFileName;
    }

    private void validateContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BusinessException(BusinessErrorCode.INVALID_PROFILE_IMAGE_FORMAT);
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(BusinessErrorCode.INVALID_PROFILE_IMAGE_SIZE);
        }
    }
}
