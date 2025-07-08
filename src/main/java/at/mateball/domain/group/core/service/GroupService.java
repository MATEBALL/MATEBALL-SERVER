package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.*;
import at.mateball.domain.group.api.dto.DirectGetRes;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.DirectGetListRes;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MatchRequirementService matchRequirementService;

    public GroupService(GroupRepository groupRepository, MatchRequirementService matchRequirementService) {
        this.groupRepository = groupRepository;
        this.matchRequirementService = matchRequirementService;
    }

    public DirectCreateRes getDirectMatching(Long userId, Long matchId) {
        DirectCreateRes result = groupRepository.findDirectCreateResults(userId, matchId);

        if (result == null) {
            throw new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND);
        }

        return result;
    }

    public DirectGetListRes getDirects(Long userId, LocalDate date) {
        LocalDate today = LocalDate.now();

        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_MONDAY);
        }

        if (date.isBefore(today)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_PAST);
        }

        LocalDate minAvailableDate = today.plusDays(2);
        if (minAvailableDate.getDayOfWeek() == DayOfWeek.MONDAY) {
            minAvailableDate = today.plusDays(3);
        }

        if (date.isBefore(minAvailableDate)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_DATE);
        }

        List<DirectGetBaseRes> result = groupRepository.findDirectGroupsByDate(date);

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        List<DirectGetRes> directGetRes = result.stream()
                .map(res -> res.withMatchRate(matchRateMap.getOrDefault(res.leaderId(), 0)))
                .map(DirectGetRes::from)
                .sorted(Comparator.comparingInt(DirectGetRes::matchRate).reversed())
                .toList();

        return new DirectGetListRes(directGetRes);
    }

    public GroupCreateRes getGroupMatching(Long userId, Long matchId) {
        return groupRepository.findGroupCreateRes(matchId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));
    }

    public GroupGetListRes getGroups(Long userId, LocalDate date) {
        LocalDate today = LocalDate.now();

        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_MONDAY);
        }

        if (date.isBefore(today)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_PAST);
        }

        LocalDate minAvailableDate = today.plusDays(2);
        if (minAvailableDate.getDayOfWeek() == DayOfWeek.MONDAY) {
            minAvailableDate = today.plusDays(3);
        }

        if (date.isBefore(minAvailableDate)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_DATE);
        }

        List<GroupGetBaseRes> baseList = groupRepository.findGroupsWithBaseInfo(date);
        List<Long> groupIds = baseList.stream().map(GroupGetBaseRes::id).toList();

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        Map<Long, Integer> countMap = groupRepository.findGroupMemberCountMap(groupIds);
        Map<Long, List<String>> imgMap = groupRepository.findGroupMemberImgMap(groupIds);

        List<GroupGetRes> result = baseList.stream()
                .map(base -> {
                    int count = countMap.getOrDefault(base.id(), 1);
                    List<String> imgs = imgMap.getOrDefault(base.id(), List.of());
                    int score = matchRateMap.getOrDefault(base.id(), 0);
                    int avg = Math.round((float) score / count);
                    return GroupGetRes.from(base, avg, count, imgs);
                })
                .sorted(Comparator.comparingInt(GroupGetRes::matchRate).reversed())
                .toList();

        return new GroupGetListRes(result);
    }
}
