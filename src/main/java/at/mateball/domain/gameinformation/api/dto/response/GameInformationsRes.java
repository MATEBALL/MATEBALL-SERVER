package at.mateball.domain.gameinformation.api.dto.response;

import java.util.List;

public record GameInformationsRes(
        List<GameInformationRes> gameSchedule
) {
}
