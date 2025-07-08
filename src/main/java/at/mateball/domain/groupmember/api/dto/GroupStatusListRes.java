package at.mateball.domain.groupmember.api.dto;

import java.util.List;

public record GroupStatusListRes(
        List<GroupStatusRes> mates
) {
}
