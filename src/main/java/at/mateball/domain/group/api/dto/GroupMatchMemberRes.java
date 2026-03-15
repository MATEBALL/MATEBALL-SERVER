package at.mateball.domain.group.api.dto;

public record GroupMatchMemberRes(
        Long memberId,
        String nickname,
        String team,
        String type,
        String img
) {
}
