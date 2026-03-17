
package at.mateball.domain.group.api.dto;

public record RequestValidationRes(
        boolean isDuplicatedRequest,
        boolean hasPendingRequest
) {
}
