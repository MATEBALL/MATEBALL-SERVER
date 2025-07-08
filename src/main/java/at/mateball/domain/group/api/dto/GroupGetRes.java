package at.mateball.domain.group.api.dto;

import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;

import java.time.LocalDate;
import java.util.List;

public record GroupGetRes(
        Long id,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        Integer matchRate,
        Integer count,
        List<String> imgUrl
) {
    public static GroupGetRes from(GroupGetBaseRes base, Integer matchRate, Integer count, List<String> imgUrls) {
        return new GroupGetRes(
                base.id(),
                base.nickname(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                matchRate,
                count,
                imgUrls
        );
    }
}
