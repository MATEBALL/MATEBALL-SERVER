package at.mateball.domain.group.api.dto;

import at.mateball.domain.group.core.Group;

import java.time.LocalDate;

public record GroupValidationRes(
        Long leaderId,
        LocalDate gameDate,
        int status
) {
    public static GroupValidationRes from(Group group) {
        return new GroupValidationRes(
                group.getLeader().getId(),
                group.getGameInformation().getGameDate(),
                group.getStatus()
        );
    }
}
