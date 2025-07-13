package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record GroupMemberBaseRes(
        LocalDate gameDate,
        Long totalMatches,
        boolean hasMatchOnSameDate
) {
}
