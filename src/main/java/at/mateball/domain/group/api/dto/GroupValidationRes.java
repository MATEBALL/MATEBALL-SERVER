package at.mateball.domain.group.api.dto;

import java.time.LocalDate;

public record GroupValidationRes(
        Long leaderId,
        LocalDate gameDate,
        int status
) {
}
