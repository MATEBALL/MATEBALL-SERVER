package at.mateball.domain.group.infrastructure.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;

import java.time.LocalDate;

public record RequestGroupQueryDto(
        Long matchId,
        String nickname,
        Integer count,
        Boolean isGroup,
        String awayTeam,
        String homeTeam,
        LocalDate date,
        Integer status
) {
    public GroupMemberStatus statusEnum() {
        return GroupMemberStatus.from(status);
    }
}
