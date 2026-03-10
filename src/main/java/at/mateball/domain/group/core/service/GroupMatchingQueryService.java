package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.GroupMemberMatchingRateRes;
import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.infrastructure.dto.GroupMemberMatchingQueryDto;
import at.mateball.domain.group.infrastructure.repository.GroupMatchingQueryRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMatchingQueryService {

    private final GroupMatchingQueryRepository groupMatchingQueryRepository;
    private final MatchingScoreCalculator matchingScoreCalculator;

    public List<GroupMemberMatchingRateRes> getGroupMemberMatchingRates(Long loginUserId, Long groupId) {
        List<GroupMemberMatchingQueryDto> rows =
                groupMatchingQueryRepository.findGroupMembersForMatching(loginUserId, groupId);

        if (rows.isEmpty()) {
            return List.of();
        }

        MatchingTarget loginUserTarget = extractLoginUserTarget(rows.get(0));
        List<GroupMemberMatchingRateRes> result = new ArrayList<>(rows.size());

        for (GroupMemberMatchingQueryDto row : rows) {
            int matchingRate = matchingScoreCalculator.calculate(loginUserTarget, row.toMemberTarget());

            result.add(new GroupMemberMatchingRateRes(
                    row.memberId(),
                    matchingRate
            ));
        }

        return result;
    }

    private MatchingTarget extractLoginUserTarget(GroupMemberMatchingQueryDto row) {
        MatchingTarget loginUserTarget = row.toLoginUserTarget();

        if (loginUserTarget.teamAllowed() == null || loginUserTarget.style() == null) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM);
        }

        return loginUserTarget;
    }
}
