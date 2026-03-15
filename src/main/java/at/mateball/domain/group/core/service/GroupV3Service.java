package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.GroupMatchMemberListRes;
import at.mateball.domain.group.api.dto.GroupMatchMemberRes;
import at.mateball.domain.group.api.dto.GroupMatchRes;
import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.core.calculator.common.GroupMatchAggregator;
import at.mateball.domain.group.infrastructure.dto.GameInfoQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchMeberQueryDto;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.domain.matchrequirement.core.constant.StyleMatch;
import at.mateball.domain.team.core.TeamNameMatch;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupV3Service {

    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final GroupMatchAggregator groupMatchAggregator;
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
        validateNotOwnMatch(userId, matchId);

        List<GroupMatchMeberQueryDto> members =
                groupV3RepositoryCustom.findMatchMembersByMatchId(matchId);

        List<GroupMatchMemberRes> results = members.stream()
                .map(this::toGroupMatchMemberRes)
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

    private GroupMatchMemberRes toGroupMatchMemberRes(GroupMatchMeberQueryDto member) {
        return new GroupMatchMemberRes(
                member.memberId(),
                member.nickname(),
                resolveTeamLabel(member.team()),
                resolveStyleLabel(member.style()),
                resolveProfileImageUrl(member.profileImageKey())
        );
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