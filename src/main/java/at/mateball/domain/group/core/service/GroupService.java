package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.ClientDirectGetRes;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.GroupBaseDto;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.api.dto.DirectGetListRes;
import at.mateball.domain.group.api.dto.DirectGetRes;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

        List<DirectGetRes> result = groupRepository.findDirectGroupsAfterDate(date);

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        List<ClientDirectGetRes> clientDirectGetRes = result.stream()
                .map(res -> res.withMatchRate(matchRateMap.getOrDefault(res.id(),0 )))
                .map(ClientDirectGetRes::from)
                .toList();

        return new DirectGetListRes(clientDirectGetRes);
    }

    public GroupCreateRes getGroupMatching(Long userId, Long matchId) {
        return groupRepository.findGroupCreateRes(matchId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));
    }
}
