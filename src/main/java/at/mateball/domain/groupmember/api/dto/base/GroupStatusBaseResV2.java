package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record GroupStatusBaseResV2(
        Long id,
        Long leaderId,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        Integer status
) {}
