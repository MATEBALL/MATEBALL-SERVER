package at.mateball.domain.group.api.dto;

import java.time.LocalDate;
import java.util.List;

public record RequestGroupRes(
        Long matchId,
        String nickname,
        Integer count,
        Boolean isGroup,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        String stateLabel,
        String update,
        List<String> imageUrls
) {
}
