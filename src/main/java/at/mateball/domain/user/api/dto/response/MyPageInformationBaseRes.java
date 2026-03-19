package at.mateball.domain.user.api.dto.response;

import at.mateball.domain.user.core.User;

public record MyPageInformationBaseRes(
        User user,
        Integer team,
        Integer style,
        Long matchCnt
) {
}
