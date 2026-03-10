package at.mateball.domain.group.api.dto;

import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;

import java.time.LocalDate;
import java.util.List;

public record GroupMatchRes(
        String awayTeam,
        String homeTeam,
        LocalDate date,
        String stadium,
        List<GroupMatchBaseRes> result
) {
}
