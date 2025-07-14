package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record GroupInformationRes(
        Long userId,
        Long groupId,
        int status,
        LocalDate gameDate,
        boolean isGroup,
        int groupStatus
) {
}
