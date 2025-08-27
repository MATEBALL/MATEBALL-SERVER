package at.mateball.domain.matchrequirement.api.dto.response;

public record MatchRequirementRes(
        String team,
        String teamAllowed,
        String style,
        String genderPreference
) {
}
