package at.mateball.domain.group.infrastructure.dto;

import java.time.LocalDate;

public record CreateGroupQueryDto(
        Long matchId,
        String nickname,
        Integer count,
        Boolean isGroup,
        String awayTeam,
        String homeTeam,
        LocalDate date,
        Integer status,
        Boolean hasNewRequest
) {
}
