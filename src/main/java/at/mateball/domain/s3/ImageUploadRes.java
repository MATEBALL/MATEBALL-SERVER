package at.mateball.domain.s3;

public record ImageUploadRes(
        String objectKey,
        String originalFileName,
        String contentType
) {
}
