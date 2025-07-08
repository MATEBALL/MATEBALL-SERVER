package at.mateball.domain.user.api.dto.request;

public record MatchRequirementReq(
        String team,
        String teamAllowed,
        String style,
        String genderPreference
) {
}
