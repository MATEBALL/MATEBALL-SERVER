package at.mateball.domain.group.api.dto.base;

import java.util.List;

public record GroupMatchBaseRes(
        Long matchId,
        String nickname,
        int count,
        boolean isGroup,
        Integer matchRate,
        List<String> img
) {
}
