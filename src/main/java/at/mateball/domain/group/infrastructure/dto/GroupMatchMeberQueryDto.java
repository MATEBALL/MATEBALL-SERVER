package at.mateball.domain.group.infrastructure.dto;

public record GroupMatchMeberQueryDto(
        Long memberId,
        String nickname,
        Integer team,
        Integer style,
        String profileImageKey
) {
}
