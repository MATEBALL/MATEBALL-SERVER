package at.mateball.domain.s3.api.dto;

public record ImageUploadRes(
        String objectKey,
        String originalFileName,
        String contentType
) {
}
