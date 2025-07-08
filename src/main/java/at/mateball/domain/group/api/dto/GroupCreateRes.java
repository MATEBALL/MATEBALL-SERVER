package at.mateball.domain.group.api.dto;

import at.mateball.domain.team.core.TeamName;

import java.time.LocalDate;
import java.util.List;

public record GroupCreateRes(
        Long id,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        Integer count,
        List<String> imgUrl
) {
    public static GroupCreateRes from(GroupBaseDto base, Integer count, List<String> imgUrls) {
        return new GroupCreateRes(
                base.id(),
                base.nickname(),
                TeamName.from(base.awayTeam()).getLabel(),
                TeamName.from(base.homeTeam()).getLabel(),
                base.stadium(),
                base.date(),
                count,
                imgUrls
        );
    }
}

