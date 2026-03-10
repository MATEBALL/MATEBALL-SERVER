package at.mateball.domain.group.infrastructure.dto;

import at.mateball.domain.group.core.calculator.MatchingTarget;

public record GroupMatchCandidateFlatDto(
        Long groupId,
        Long leaderId,
        String leaderNickname,
        boolean isGroup,

        Long memberUserId,
        String memberImgUrl,
        String memberProfileImageKey,

        Integer memberTeam,
        Integer memberTeamAllowed,
        Integer memberStyle
) {
    public MatchingTarget toMemberTarget() {
        return new MatchingTarget(
                memberUserId,
                memberTeam,
                memberTeamAllowed,
                memberStyle
        );
    }
}
