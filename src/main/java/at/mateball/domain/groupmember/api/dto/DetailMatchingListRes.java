package at.mateball.domain.groupmember.api.dto;

import java.util.List;

public record DetailMatchingListRes(
        String nickname,
        List<DetailMatchingRes> mates
) {
}
