package at.mateball.domain.matchrequirement.api.dto;

public record MatchingScoreDto(
        Long targetUserId,
        int teamScore,
        int genderScore,
        int styleScore
) {
    public int totalScore() {
        return teamScore + genderScore + styleScore;
    }
}
