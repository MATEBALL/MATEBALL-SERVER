package at.mateball.domain.user.api.dto.response;

public record UserInformationBaseRes(
        String nickname,
        Integer birthYear,
        String gender,
        Integer team,
        Integer style,
        String introduction,
        String imgUrl
) {
}
