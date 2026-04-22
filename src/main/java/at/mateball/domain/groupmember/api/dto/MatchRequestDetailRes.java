package at.mateball.domain.groupmember.api.dto;

public record MatchRequestDetailRes(
        Long id,
        String nickname,
        String age,
        String gender,
        String team,
        String style,
        String introduction,
        String imgUrl,
        Integer avgGame,
        Integer avgSeason,
        Long matchRate
) {
}
