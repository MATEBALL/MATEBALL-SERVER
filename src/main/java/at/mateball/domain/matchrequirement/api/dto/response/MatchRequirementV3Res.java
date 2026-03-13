package at.mateball.domain.matchrequirement.api.dto.response;

public record MatchRequirementV3Res(
        String team,
        String teamAllowed,
        String style,
        int avgSeason
) {
}
