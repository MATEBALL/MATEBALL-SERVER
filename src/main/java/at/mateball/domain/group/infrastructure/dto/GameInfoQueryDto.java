package at.mateball.domain.group.infrastructure.dto;

import java.time.LocalDate;

public record GameInfoQueryDto(
        String awayTeam,
        String homeTeam,
        LocalDate date,
        String stadium
) {
}
