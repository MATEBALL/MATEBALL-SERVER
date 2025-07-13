package at.mateball.domain.groupmember.api.dto;

import java.time.LocalDate;

public record GroupMemberRes(
        LocalDate gameDate,
        Long totalMatches,
        boolean hasMatchOnSameDate
) {
}
