package at.mateball.domain.group.core.calculator.common;

import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GroupMatchAggregator {

    private final MatchingScoreCalculator matchingScoreCalculator;

    public List<GroupMatchBaseRes> aggregate(
            List<GroupMatchCandidateFlatDto> flatRows,
            MatchingTarget loginUserTarget,
            Long loginUserId,
            Map<Long, List<String>> imageMap
    ) {
        Map<Long, MatchAggregate> aggregateMap = new LinkedHashMap<>();

        for (GroupMatchCandidateFlatDto row : flatRows) {
            MatchAggregate aggregate = aggregateMap.computeIfAbsent(
                    row.groupId(),
                    ignored -> new MatchAggregate(
                            row.groupId(),
                            row.leaderId(),
                            row.leaderNickname(),
                            row.isGroup()
                    )
            );

            aggregate.increaseCount();

            if (Objects.equals(row.memberUserId(), loginUserId)) {
                continue;
            }

            if (Objects.equals(row.leaderId(), loginUserId)) {
                continue;
            }

            int score = matchingScoreCalculator.calculate(loginUserTarget, row.toMemberTarget());
            aggregate.addScore(score);
        }

        return aggregateMap.values().stream()
                .map(aggregate -> aggregate.toResponse(imageMap.getOrDefault(aggregate.matchId(), Collections.emptyList())))
                .toList();
    }

    private static final class MatchAggregate {
        private final Long matchId;
        private final Long leaderId;
        private final String nickname;
        private final boolean isGroup;

        private int count;
        private int scoreSum;
        private int scoreCount;

        private MatchAggregate(Long matchId, Long leaderId, String nickname, boolean isGroup) {
            this.matchId = matchId;
            this.leaderId = leaderId;
            this.nickname = nickname;
            this.isGroup = isGroup;
        }

        private Long matchId() {
            return matchId;
        }

        private void increaseCount() {
            count++;
        }

        private void addScore(int score) {
            scoreSum += score;
            scoreCount++;
        }

        private GroupMatchBaseRes toResponse(List<String> images) {
            Integer matchRate = null;
            if (scoreCount > 0) {
                matchRate = (int) Math.round((double) scoreSum / scoreCount);
            }

            return new GroupMatchBaseRes(
                    matchId,
                    nickname,
                    count,
                    isGroup,
                    matchRate,
                    images
            );
        }
    }
}