package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;

import java.time.LocalDate;
import java.util.List;

public record GroupStatusRes(
        Long id,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        String status,
        Integer count,
        List<String> imgUrl
) {
    public static GroupStatusRes from(GroupStatusBaseRes base, Integer count, List<String> imgUrls) {
        return new GroupStatusRes(
                base.id(),
                base.nickname(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                GroupMemberStatus.from(base.status()).getLabel(),
                count,
                imgUrls
        );
    }
}
