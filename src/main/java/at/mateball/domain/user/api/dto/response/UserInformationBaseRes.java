package at.mateball.domain.user.api.dto.response;

public record UserInformationBaseRes(
        String nickname,
        int birthYear,
        String gender,
        Integer style,
        String introduction,
        String imgUrl
) {}
