package at.mateball.domain.user.api.dto.request;

public record UserInfoReq(
        String gender,
        int birthYear
) {
}
