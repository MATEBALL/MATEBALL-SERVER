package at.mateball.domain.matchrequirement.api.dto.response;

public record MatchRequirementAndAvgSeasonRes(
        String team,
        String teamAllowed,
        String style,
        int avgSeason
) {
}
