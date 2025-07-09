package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.*;
import at.mateball.domain.groupmember.api.dto.base.DetailMatchingBaseRes;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final MatchRequirementService matchRequirementService;

    public GroupMemberService(GroupMemberRepository groupMemberRepository, MatchRequirementService matchRequirementService) {
        this.groupMemberRepository = groupMemberRepository;
        this.matchRequirementService = matchRequirementService;
    }

    public DirectStatusListRes getDirectStatus(Long userId, GroupStatus groupStatus) {
        List<DirectStatusBaseRes> baseResList = groupMemberRepository.findDirectMatchingsByUserAndGroupStatus(userId, groupStatus.getValue());

        List<DirectStatusRes> result = baseResList.stream()
                .map(DirectStatusRes::from)
                .toList();

        return new DirectStatusListRes(result);
    }

    public DirectStatusListRes getAllDirectStatus(Long userId) {
        List<DirectStatusBaseRes> baseResList = groupMemberRepository.findAllDirectMatchingsByUser(userId);

        List<DirectStatusRes> result = baseResList.stream()
                .map(DirectStatusRes::from)
                .toList();

        return new DirectStatusListRes(result);
    }

    public GroupStatusListRes getAllGroupStatus(Long userId) {
        List<GroupStatusBaseRes> baseResList = groupMemberRepository.findGroupMatchingsByUser(userId);
        return mapWithCountsAndImages(baseResList);
    }

    public GroupStatusListRes getGroupStatus(Long userId, GroupStatus status) {
        List<GroupStatusBaseRes> baseResList = groupMemberRepository.findGroupMatchingsByUserAndStatus(userId, status.getValue());
        return mapWithCountsAndImages(baseResList);
    }

    public DetailMatchingListRes getDetailMatching(Long userId, Long matchId) {
        List<DetailMatchingBaseRes> baseResList = groupMemberRepository.findGroupMatesByMatchId(userId, matchId);

        if (baseResList == null || baseResList.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND);
        }

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        List<DetailMatchingRes> result = baseResList.stream()
                .map(base -> {
                    Integer matchRate = matchRateMap.getOrDefault(base.userId(), 0);
                    return DetailMatchingRes.from(base, matchRate);
                })
                .toList();

        return new DetailMatchingListRes(result);
    }

    private GroupStatusListRes mapWithCountsAndImages(List<GroupStatusBaseRes> baseResList) {
        if (baseResList.isEmpty()) {
            return new GroupStatusListRes(List.of());
        }

        List<Long> groupIds = baseResList.stream()
                .map(GroupStatusBaseRes::id)
                .toList();

        Map<Long, Integer> countMap = groupMemberRepository.findGroupMemberCountMap(groupIds);
        Map<Long, List<String>> imgMap = groupMemberRepository.findGroupMemberImgMap(groupIds);

        List<GroupStatusRes> result = baseResList.stream()
                .map(res -> GroupStatusRes.from(
                        res,
                        countMap.getOrDefault(res.id(), 0),
                        imgMap.getOrDefault(res.id(), List.of())
                ))
                .toList();

        return new GroupStatusListRes(result);
    }

    public GroupMemberCountRes countGroupMember(final Long matchId) {
        return groupMemberRepository.countGroupMember(matchId);
    }
}
