package at.mateball.domain.group.api.dto;

import java.time.LocalDate;

public record MatchValidationDto(
        LocalDate gameDate,
        Boolean existsMatchOnSameGameInformation
) {}
