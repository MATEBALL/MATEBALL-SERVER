package at.mateball.domain.groupmember.api.dto;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseResV2;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record GroupStatusResV2(

        Long id,
        String nickname,
        String awayTeam,
        String homeTeam,
        String stadium,
        LocalDate date,
        String status,
        Integer count,
        List<String> imgUrl,
        boolean isCreated
) {
    public static GroupStatusResV2 from(GroupStatusBaseResV2 base, Integer count, List<String> imgUrls, Long userId) {
        boolean isCreated = base.leaderId().equals(userId);
        return new GroupStatusResV2(
                base.id(),
                base.nickname(),
                base.awayTeam(),
                base.homeTeam(),
                base.stadium(),
                base.date(),
                GroupMemberStatus.from(base.status()).getLabel(),
                count,
                imgUrls,
                isCreated
        );
    }
}
