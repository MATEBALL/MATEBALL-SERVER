package at.mateball.domain.groupmember.api.dto.base;

import java.time.LocalDate;

public record GroupStatusBaseRes(
     Long id,
     String nickname,
     String awayTeam,
     String homeTeam,
     String stadium,
     LocalDate date,
     Integer status
) {
}
