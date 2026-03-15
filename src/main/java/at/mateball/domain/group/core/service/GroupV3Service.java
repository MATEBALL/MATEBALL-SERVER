package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.CreateGroupListRes;
import at.mateball.domain.group.api.dto.CreateGroupRes;
import at.mateball.domain.group.api.dto.GroupMatchRes;
import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.assembler.MatchImageAssembler;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.core.calculator.common.GroupMatchAggregator;
import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupV3Service {

    private static final String NEW_REQUEST_LABEL = "새요청";

    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final GroupMatchAggregator groupMatchAggregator;
    private final MatchImageAssembler matchImageAssembler;

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

        List<GroupMatchBaseRes> result = new java.util.ArrayList<>(
                groupMatchAggregator.aggregate(flatRows, loginUserTarget, userId, imageMap)
        );

        result.sort(
                java.util.Comparator
                        .comparing(GroupMatchBaseRes::matchRate,
                                java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder()))
                        .thenComparing(GroupMatchBaseRes::count, java.util.Comparator.reverseOrder())
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

    private String resolveUpdateLabel(Boolean hasNewRequest) {
        return Boolean.TRUE.equals(hasNewRequest) ? NEW_REQUEST_LABEL : null;
    }
}