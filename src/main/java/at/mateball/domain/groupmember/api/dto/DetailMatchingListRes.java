package at.mateball.domain.groupmember.api.dto;

import java.util.List;

public record DetailMatchingListRes(
        List<DetailMatchingRes> mates
) {
}
