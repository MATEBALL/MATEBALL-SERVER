package at.mateball.domain.group.core.service;

import at.mateball.domain.alarm.common.AlarmType;
import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.chatting.api.dto.response.ChattingRes;
import at.mateball.domain.gameinformation.core.repository.GameInformationRepository;
import at.mateball.domain.chatting.api.dto.response.ChattingRes;
import at.mateball.domain.group.api.dto.*;
import at.mateball.domain.group.api.dto.base.GroupMatchBaseRes;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupExecutorV3;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.MatchType;
import at.mateball.domain.group.core.assembler.MatchImageAssembler;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.core.calculator.common.GroupMatchAggregator;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.matchrequirement.core.constant.StyleMatch;
import at.mateball.domain.team.core.TeamNameMatch;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.storage.FileStorage;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static at.mateball.domain.group.core.validator.DateValidator.validate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupV3Service {

    private static final String NEW_REQUEST_LABEL = "새요청";

    private final GroupRepository groupRepository;
    private final GameInformationRepository gameInformationRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final AlarmService alarmService;
    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final GroupMatchAggregator groupMatchAggregator;
    private final MatchImageAssembler matchImageAssembler;
    private final MatchingScoreCalculator matchingScoreCalculator;
    private final FileStorage fileStorage;
    private final GroupExecutorV3 groupExecutor;

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
                ).stream()
                .sorted(
                        Comparator.comparing(
                                        GroupMatchBaseRes::matchRate,
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                                .thenComparing(GroupMatchBaseRes::count, Comparator.reverseOrder())
                                .thenComparing(GroupMatchBaseRes::matchId)
                )
                .toList();

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

    public RequestGroupListRes getRequestGroupList(Long userId) {
        List<RequestGroupQueryDto> groups = groupV3RepositoryCustom.findRequestGroupsByUserId(userId);

        if (groups.isEmpty()) {
            return new RequestGroupListRes(List.of());
        }

        validateRequestGroupStatuses(groups);

        List<Long> matchIds = groups.stream()
                .map(RequestGroupQueryDto::matchId)
                .toList();

        Map<Long, List<String>> imageMap = matchImageAssembler.assemble(
                groupV3RepositoryCustom.findRequestGroupImagesByMatchIds(matchIds)
        );

        List<RequestGroupRes> results = groups.stream()
                .map(group -> {
                    GroupMemberStatus status = group.statusEnum();

                    return new RequestGroupRes(
                            group.matchId(),
                            group.nickname(),
                            group.count(),
                            group.isGroup(),
                            group.awayTeam(),
                            group.homeTeam(),
                            group.date(),
                            status.toResponseLabel(),
                            resolveRequestUpdateLabel(status),
                            imageMap.getOrDefault(group.matchId(), Collections.emptyList())
                    );
                })
                .toList();

        return new RequestGroupListRes(results);
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

    private void validateRequestGroupStatuses(List<RequestGroupQueryDto> groups) {
        boolean hasPendingRequest = groups.stream()
                .anyMatch(group -> group.statusEnum() == GroupMemberStatus.PENDING_REQUEST);

        if (hasPendingRequest) {
            throw new BusinessException(BusinessErrorCode.WAITING_MATE_ACCEPTANCE);
        }
    }

    private String resolveRequestUpdateLabel(GroupMemberStatus status) {
        return status == GroupMemberStatus.MATCH_FAILED ? GroupMemberStatus.MATCH_FAILED.getLabel() : null;
    }

    public ChattingRes getChattingUrl(Long userId, Long matchId) {

        ChattingAccessRes data = groupRepository.findChattingAccessInfo(userId, matchId);

        if (data == null) {
            throw new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND);
        }
        if (data.chattingUrl() == null) {
            throw new BusinessException(BusinessErrorCode.CHATTING_NOT_FOUND);
        }

        GroupMemberStatus status = GroupMemberStatus.from(data.memberStatus());

        if (status == GroupMemberStatus.AWAITING_APPROVAL
                || status == GroupMemberStatus.MATCH_FAILED) {
            throw new BusinessException(BusinessErrorCode.INVALID_CHATTING_REQUEST_MEMBER);
        }

        return new ChattingRes(data.chattingUrl());
    }

    @Transactional
    public CreateMatchRes createMatch(Long userId, Long gameId, MatchType matchType) {

        boolean isGroup = isGroupMatch(matchType);

        MatchValidationRes data = groupV3RepositoryCustom.getMatchValidationInfo(userId, gameId);

        if (data == null) {
            throw new BusinessException(BusinessErrorCode.GAME_NOT_FOUND);
        }

        validate(data.gameDate());

        if (data.existsMatchOnSameGameInformation()) {
            throw new BusinessException(BusinessErrorCode.EXCEED_MATCHING_LIMIT);
        }

        return new CreateMatchRes(
                groupExecutor.createGroup(userId, gameId, isGroup)
        );
    }

    private boolean isGroupMatch(MatchType matchType) {
        return matchType == MatchType.GROUP;
    }

    @Transactional
    public void createRequest(Long userId, Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        validateRequest(userId, group);

        try {
            groupMemberRepository.createGroupMember(userId, groupId);
            groupMemberRepository.updateMemberStatus(group.getLeader().getId(), group.getId(), GroupMemberStatus.NEW_REQUEST.getValue());

            alarmService.createAlarm(group.getLeader().getId(), AlarmType.NEW_REQUEST, group.getId());
        } catch (DataIntegrityViolationException | PersistenceException e) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_MATCH_REQUEST);
        }
    }

    private void validateRequest(Long userId, Group group) {

        validate(group.getGameInformation().getGameDate());

        if (group.getLeader().getId().equals(userId)) {
            throw new BusinessException(BusinessErrorCode.CANNOT_REQUEST_OWN_MATCH);
        }

        if (group.getStatus() == GroupStatus.COMPLETED.getValue()) {
            throw new BusinessException(BusinessErrorCode.ALREADY_FINISHED_MATCH);
        }

        RequestValidationRes data =
                groupV3RepositoryCustom.getValidation(userId, group.getId());

        if (data.isDuplicatedRequest()) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_MATCH_REQUEST);
        }

        if (data.hasPendingRequest()) {
            throw new BusinessException(BusinessErrorCode.ALREADY_HAS_PENDING_REQUEST);
        }
    }
}
