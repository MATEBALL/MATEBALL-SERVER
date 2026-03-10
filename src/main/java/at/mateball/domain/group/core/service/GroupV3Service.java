package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.GroupMatchRes;
import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.infrastructure.dto.GameInfoQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;
import at.mateball.domain.group.infrastructure.repository.GroupV3QueryRepository;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupV3Service {

    private final GroupV3QueryRepository groupV3QueryRepository;
    private final MatchingScoreCalculator matchingScoreCalculator;

    public GroupMatchRes getGroupMatchs(Long userId, Long gameId) {
        GameInfoQueryDto gameInfo = groupV3QueryRepository.findGameInfoByGameId(gameId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GAME_NOT_FOUND));

        LoginUserMatchRequirementDto loginRequirement = groupV3QueryRepository.findLoginUserMatchRequirement(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND));

        List<GroupMatchCandidateFlatDto> flatRows =
                groupV3QueryRepository.findMatchCandidatesByGameId(userId, gameId);
        if (flatRows.isEmpty()) {
            return new GroupMatchRes(
                    gameInfo.awayTeam(),
                    gameInfo.homeTeam(),
                    gameInfo.date(),
                    gameInfo.stadium(),
                    List.of()
            );
        }

        MatchingTarget loginUserTarget = loginRequirement.toTarget(userId);

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

            aggregate.addImage(resolveProfileImageUrl(row.memberImgUrl(), row.memberProfileImageKey()));

            // 자기 자신이 멤버 row로 섞여 있으면 평균 계산에서 제외
            if (Objects.equals(row.memberUserId(), userId)) {
                continue;
            }

            // 내가 만든 매칭은 matchRate null 이므로 점수 누적 안 함
            if (Objects.equals(row.leaderId(), userId)) {
                continue;
            }

            int score = matchingScoreCalculator.calculate(loginUserTarget, row.toMemberTarget());
            aggregate.addScore(score);
        }

        List<GroupMatchBaseRes> result = aggregateMap.values().stream()
                .map(MatchAggregate::toResponse)
                .toList();

        return new GroupMatchRes(
                gameInfo.awayTeam(),
                gameInfo.homeTeam(),
                gameInfo.date(),
                gameInfo.stadium(),
                result
        );
    }

    private String resolveProfileImageUrl(String imgUrl, String profileImageKey) {
        if (profileImageKey == null || profileImageKey.isBlank()) {
            return User.DEFAULT_PROFILE_IMAGE_URL;
        }
        return (imgUrl == null || imgUrl.isBlank()) ? User.DEFAULT_PROFILE_IMAGE_URL : imgUrl;
    }

    private static final class MatchAggregate {
        private final Long matchId;
        private final Long leaderId;
        private final String nickname;
        private final boolean isGroup;
        private final List<String> images = new ArrayList<>();

        private int count;
        private int scoreSum;
        private int scoreCount;

        private MatchAggregate(Long matchId, Long leaderId, String nickname, boolean isGroup) {
            this.matchId = matchId;
            this.leaderId = leaderId;
            this.nickname = nickname;
            this.isGroup = isGroup;
        }

        private void addImage(String imageUrl) {
            images.add(imageUrl);
            count++;
        }

        private void addScore(int score) {
            scoreSum += score;
            scoreCount++;
        }

        private GroupMatchBaseRes toResponse() {
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