package at.mateball.storage.dto;

public record ImageUploadRes(
        String objectKey,
        String originalFileName,
        String contentType
) {
}
