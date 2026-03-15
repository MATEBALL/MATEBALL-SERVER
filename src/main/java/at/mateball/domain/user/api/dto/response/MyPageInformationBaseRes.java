package at.mateball.domain.user.api.dto.response;

public record MyPageInformationBaseRes(
        String nickname,
        Integer team,
        Integer style,
        String imgUrl,
        Long matchCnt,
        Integer avgSeason
) {

}
