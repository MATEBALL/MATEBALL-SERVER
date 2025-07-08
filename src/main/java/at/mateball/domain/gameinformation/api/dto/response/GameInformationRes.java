package at.mateball.domain.gameinformation.api.dto.response;

import at.mateball.domain.gameinformation.core.GameInformation;

public record GameInformationRes(
        Long id,
        String awayTeam,
        String homeTeam,
        String gameTime,
        String stadium
) {
    public static GameInformationRes from(GameInformation game) {
        return new GameInformationRes(
                game.getId(),
                game.getAwayTeamName(),
                game.getHomeTeamName(),
                game.getGameTime().toString(),
                game.getStadiumName()
        );
    }
}
