package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.GroupMatchMemberListRes;
import at.mateball.domain.group.api.dto.GroupMatchMemberRes;
import at.mateball.domain.group.api.dto.GroupMatchRes;
import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.core.calculator.common.GroupMatchAggregator;
import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.domain.matchrequirement.core.constant.StyleMatch;
import at.mateball.domain.team.core.TeamNameMatch;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupV3Service {

    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final GroupMatchAggregator groupMatchAggregator;
    private final MatchingScoreCalculator matchingScoreCalculator;
    private final FileStorage fileStorage;

    public GroupMatchRes getGroupMatches(Long userId, Long gameId) {
        GameInfoQueryDto gameInfo = groupV3RepositoryCustom.findGameInfoByGameId(gameId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GAME_NOT_FOUND));

        LoginUserMatchRequirementDto loginRequirement = groupV3RepositoryCustom.findLoginUserMatchRequirement(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND));

        List<GroupMatchCandidateFlatDto> flatRows =
                groupV3RepositoryCustom.findMatchCandidatesByGameId(userId, gameId);

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

        List<GroupMatchBaseRes> result =
                groupMatchAggregator.aggregate(flatRows, loginUserTarget, userId);

        return new GroupMatchRes(
                gameInfo.awayTeam(),
                gameInfo.homeTeam(),
                gameInfo.date(),
                gameInfo.stadium(),
                result
        );
    }

    public GroupMatchMemberListRes getMatchGroupMembers(Long userId, Long matchId) {

        LoginUserMatchRequirementDto loginRequirement = groupV3RepositoryCustom.findLoginUserMatchRequirement(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND));

        List<GroupMatchMemberQueryDto> members =
                groupV3RepositoryCustom.findMatchMembersByMatchId(matchId);

        MatchingTarget loginUserTarget = loginRequirement.toTarget(userId);

        List<Long> memberIds = members.stream()
                .map(GroupMatchMemberQueryDto::memberId)
                .toList();

        Map<Long, Integer> matchCountMap = groupV3RepositoryCustom.countGroupMembersByUserIds(memberIds).stream()
                .collect(Collectors.toMap(
                        MemberMatchCountDto::memberId,
                        MemberMatchCountDto::matchCount
                ));

        List<GroupMatchMemberRes> results = members.stream()
                .map(member -> toGroupMatchMemberRes(
                        member,
                        loginUserTarget,
                        matchCountMap.getOrDefault(member.memberId(), 0)
                ))
                .toList();

        return new GroupMatchMemberListRes(results);
    }

    private GroupMatchMemberRes toGroupMatchMemberRes(
            GroupMatchMemberQueryDto member,
            MatchingTarget loginUserTarget,
            Integer matchCount
    ) {
        Long matchRate = (long) matchingScoreCalculator.calculate(
                loginUserTarget,
                member.toMatchingTarget()
        );

        return new GroupMatchMemberRes(
                member.memberId(),
                matchRate,
                resolveAge(member.birthYear()),
                member.gender(),
                member.nickname(),
                member.introduction(),
                resolveTeamLabel(member.team()),
                resolveStyleLabel(member.style()),
                matchCount,
                member.avgSeason(),
                resolveProfileImageUrl(member.profileImageKey())
        );
    }

    private Long resolveAge(Integer birthYear) {
        if (birthYear == null) {
            return null;
        }
        int currentYear = LocalDate.now().getYear();
        return (long) (currentYear - birthYear + 1);
    }

    private String resolveTeamLabel(Integer team) {
        return TeamNameMatch.from(team).getLabel();
    }

    private String resolveStyleLabel(Integer style) {
        return StyleMatch.from(style).getLabel();
    }

    private String resolveProfileImageUrl(String profileImageKey) {
        return fileStorage.getImageUrl(profileImageKey);
    }
}
