package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.CreateGroupListRes;
import at.mateball.domain.group.api.dto.CreateGroupRes;
import at.mateball.domain.group.api.dto.GroupMatchMemberListRes;
import at.mateball.domain.group.api.dto.GroupMatchMemberRes;
import at.mateball.domain.group.api.dto.GroupMatchRes;
import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.assembler.MatchImageAssembler;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.core.calculator.common.GroupMatchAggregator;
import at.mateball.domain.group.infrastructure.dto.CreateGroupQueryDto;
import at.mateball.domain.group.infrastructure.dto.GameInfoQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchMemberQueryDto;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;
import at.mateball.domain.group.infrastructure.dto.MemberMatchCountDto;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.domain.matchrequirement.core.constant.StyleMatch;
import at.mateball.domain.team.core.TeamNameMatch;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupV3Service {

    private static final String NEW_REQUEST_LABEL = "새요청";

    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final GroupMatchAggregator groupMatchAggregator;
    private final MatchImageAssembler matchImageAssembler;
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

        Map<Long, List<String>> imageMap = matchImageAssembler.assemble(
                groupV3RepositoryCustom.findMatchImagesByGameId(gameId)
        );

        List<GroupMatchBaseRes> result = groupMatchAggregator.aggregate(
                flatRows,
                loginUserTarget,
                userId,
                imageMap
        );

        result.sort(
                Comparator.comparing(
                                GroupMatchBaseRes::matchRate,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                        .thenComparing(GroupMatchBaseRes::count, Comparator.reverseOrder())
                        .thenComparing(GroupMatchBaseRes::matchId)
        );

        return new GroupMatchRes(
                gameInfo.awayTeam(),
                gameInfo.homeTeam(),
                gameInfo.date(),
                gameInfo.stadium(),
                result
        );
    }

    public CreateGroupListRes getCreateGroupList(Long userId) {
        List<CreateGroupQueryDto> groups = groupV3RepositoryCustom.findCreateGroupsByUserId(userId);

        if (groups.isEmpty()) {
            return new CreateGroupListRes(List.of());
        }

        List<Long> matchIds = groups.stream()
                .map(CreateGroupQueryDto::matchId)
                .toList();

        Map<Long, List<String>> imageMap = matchImageAssembler.assemble(
                groupV3RepositoryCustom.findCreateGroupImagesByMatchIds(matchIds)
        );

        List<CreateGroupRes> results = groups.stream()
                .map(group -> new CreateGroupRes(
                        group.matchId(),
                        group.nickname(),
                        group.count(),
                        group.isGroup(),
                        group.awayTeam(),
                        group.homeTeam(),
                        group.date(),
                        GroupStatus.from(group.status()).toResponseLabel(),
                        resolveUpdateLabel(group.hasNewRequest()),
                        imageMap.getOrDefault(group.matchId(), Collections.emptyList())
                ))
                .toList();

        return new CreateGroupListRes(results);
    }

    public GroupMatchMemberListRes getMatchGroupMembers(Long userId, Long matchId) {
        validateNotOwnMatch(userId, matchId);

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

    private void validateNotOwnMatch(Long userId, Long matchId) {
        Long leaderId = groupV3RepositoryCustom.findLeaderIdByMatchId(matchId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        if (leaderId.equals(userId)) {
            throw new BusinessException(BusinessErrorCode.OWN_MATCH_MEMBER_VIEW_NOT_ALLOWED);
        }
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

    private String resolveUpdateLabel(Boolean hasNewRequest) {
        return Boolean.TRUE.equals(hasNewRequest) ? NEW_REQUEST_LABEL : null;
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