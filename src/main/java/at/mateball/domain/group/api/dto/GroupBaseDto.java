package at.mateball.domain.group.api.dto;

import java.time.LocalDate;

public record GroupBaseDto(
        Long id,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date
) {
}

