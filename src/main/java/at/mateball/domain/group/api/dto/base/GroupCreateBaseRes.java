package at.mateball.domain.group.api.dto.base;

import java.time.LocalDate;

public record GroupCreateBaseRes(
        Long id,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date
) {
}
