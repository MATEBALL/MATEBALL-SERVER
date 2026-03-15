package at.mateball.domain.group.api.dto;

public record GroupMatchMemberRes(
        Long memberId,
        Long matchRate,
        Long age,
        String gender,
        String nickname,
        String introduction,
        String team,
        String type,
        Integer avgGame,
        Integer avgSeason,
        String img
) {
}
