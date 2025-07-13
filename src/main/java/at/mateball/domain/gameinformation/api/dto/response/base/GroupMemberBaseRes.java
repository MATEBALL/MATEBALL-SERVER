package at.mateball.domain.gameinformation.api.dto.response.base;

import java.time.LocalDate;

public record GroupMemberBaseRes(
    LocalDate gameDate,
    Long totalMatches,
    boolean hasMatchOnSameDate
) {
}
