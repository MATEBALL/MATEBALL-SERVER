package at.mateball.domain.group.api.dto.base;

import java.time.LocalDate;
import java.util.List;

public record GroupGetBaseRes(
        Long id,
        Integer birthYear,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date
) {
}
