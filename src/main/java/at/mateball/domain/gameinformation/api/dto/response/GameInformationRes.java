package at.mateball.domain.gameinformation.api.dto.response;

import at.mateball.domain.gameinformation.core.GameInformation;
import io.swagger.v3.oas.annotations.media.Schema;

public record GameInformationRes(
        @Schema(description = "경기 아이디")
        Long id,
        @Schema(description = "원정팀 이름")
        String awayTeam,
        @Schema(description = "홈팀 이름")
        String homeTeam,
        @Schema(description = "경기 시간")
        String gameTime,
        @Schema(description = "경기장")
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
