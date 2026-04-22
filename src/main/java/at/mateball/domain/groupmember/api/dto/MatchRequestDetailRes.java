package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.infrastructure.dto.MatchRequestDetailQueryDto;
import at.mateball.domain.matchrequirement.core.constant.StyleMatch;
import at.mateball.domain.team.core.TeamNameMatch;

import java.time.LocalDate;

public record MatchRequestDetailRes(
        Long id,
        String nickname,
        String age,
        String gender,
        String team,
        String style,
        String introduction,
        String imgUrl,
        Integer avgGame,
        Integer avgSeason,
        Long matchRate
) {
    public static MatchRequestDetailRes of(
            MatchRequestDetailQueryDto queryDto,
            Integer avgGame,
            Long matchRate,
            String imageUrl
    ) {
        return new MatchRequestDetailRes(
                queryDto.memberId(),
                queryDto.nickname(),
                toAgeText(queryDto.birthYear()),
                queryDto.gender(),
                TeamNameMatch.from(queryDto.team()).getLabel(),
                StyleMatch.from(queryDto.style()).getLabel(),
                queryDto.introduction(),
                imageUrl,
                avgGame,
                queryDto.avgSeason(),
                matchRate
        );
    }

    private static String toAgeText(Integer birthYear) {
        if (birthYear == null) {
            return null;
        }

        int currentYear = LocalDate.now().getYear();
        return (currentYear - birthYear + 1) + "세";
    }
}