package at.mateball.domain.group.api.dto;

import java.time.LocalDate;

public record MatchValidationRes(
        LocalDate gameDate,
        Boolean existsMatchOnSameGameInformation
) {}
