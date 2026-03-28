package at.mateball.domain.group.api.dto;

import java.util.List;

public record GroupMatchMemberListRes(
        String leader,
        List<GroupMatchMemberRes> results
) {
}
