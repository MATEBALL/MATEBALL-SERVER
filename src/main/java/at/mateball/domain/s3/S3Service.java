package at.mateball.domain.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public ImageUploadRes uploadProfileImage(MultipartFile file) throws IOException {
        validateImage(file);

        String originalFileName = Objects.requireNonNull(file.getOriginalFilename(), "파일명이 없습니다.");
        String contentType = Objects.requireNonNull(file.getContentType(), "콘텐츠 타입이 없습니다.");

        String objectKey = createObjectKey("profiles", originalFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(contentType);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, objectKey, file.getInputStream(), metadata);

        amazonS3.putObject(putObjectRequest);

        return new ImageUploadRes(
                objectKey,
                originalFileName,
                contentType
        );
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String createObjectKey(String dirName, String originalFileName) {
        String safeFileName = originalFileName.replaceAll("\\s+", "_");
        return dirName + "/" + UUID.randomUUID() + "_" + safeFileName;
    }
}
