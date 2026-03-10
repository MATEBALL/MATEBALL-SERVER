package at.mateball.domain.group.core.calculator.common;

import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GroupMatchAggregator {

    private final MatchingScoreCalculator matchingScoreCalculator;
    private final GroupProfileImageResolver groupProfileImageResolver;

    public List<GroupMatchBaseRes> aggregate(
            List<GroupMatchCandidateFlatDto> flatRows,
            MatchingTarget loginUserTarget,
            Long loginUserId
    ) {
        Map<Long, GroupMatchAggregate> aggregateMap = new LinkedHashMap<>();

        for (GroupMatchCandidateFlatDto row : flatRows) {
            GroupMatchAggregate aggregate = aggregateMap.computeIfAbsent(
                    row.groupId(),
                    ignored -> new GroupMatchAggregate(
                            row.groupId(),
                            row.leaderId(),
                            row.leaderNickname(),
                            row.isGroup()
                    )
            );

            aggregate.addImage(
                    groupProfileImageResolver.resolve(row.memberProfileImageKey())
            );

            if (Objects.equals(row.memberUserId(), loginUserId)) {
                continue;
            }

            if (Objects.equals(aggregate.getLeaderId(), loginUserId)) {
                continue;
            }

            int score = matchingScoreCalculator.calculate(loginUserTarget, row.toMemberTarget());
            aggregate.addScore(score);
        }

        return aggregateMap.values().stream()
                .map(GroupMatchAggregate::toResponse)
                .toList();
    }
}
