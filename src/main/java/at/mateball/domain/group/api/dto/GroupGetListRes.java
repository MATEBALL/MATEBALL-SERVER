package at.mateball.domain.group.api.dto;

import java.util.List;

public record GroupGetListRes(
        List<GroupGetRes> mates
) {
}
