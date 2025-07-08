package at.mateball.domain.gameinformation.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GameInformationsRes(
        @Schema(description = "경기 정보 리스트")
        List<GameInformationRes> gameSchedule
) {
}
