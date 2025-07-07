package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.DirectGetListRes;
import at.mateball.domain.group.api.dto.DirectGetRes;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.domain.user.core.QUser;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import com.querydsl.core.Tuple;
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
        LocalDate baseDate = date.plusDays(2);

        if (baseDate.getDayOfWeek() == DayOfWeek.MONDAY) {
            baseDate = date.plusDays(3);
        }

        Map<Long, Integer> matchRateMap = matchRequirementService.getMatchings(userId).stream()
                .collect(Collectors.toMap(
                        MatchingScoreDto::targetUserId,
                        MatchingScoreDto::totalScore
                ));

        List<Tuple> tuples = groupRepository.findDirectGroupsAfterDate(baseDate);

        List<DirectGetRes> result = tuples.stream()
                .map(tuple -> {
                    Long targetUserId = tuple.get(QUser.user.id);
                    int matchRate = matchRateMap.getOrDefault(targetUserId, 0);
                    return DirectGetRes.from(tuple, matchRate);
                })
                .toList();

        return new DirectGetListRes(result);
    }
}
