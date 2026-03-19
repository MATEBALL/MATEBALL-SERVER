package at.mateball.domain.user.api.dto.response;

public record MyPageInformationBaseRes(
        String nickname,
        Integer team,
        Integer style,
        Long matchCnt,
        Integer avgSeason
) {

}
