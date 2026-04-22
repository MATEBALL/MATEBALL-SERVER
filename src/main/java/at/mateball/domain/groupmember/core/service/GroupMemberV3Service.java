package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.group.core.calculator.MatchingScoreCalculator;
import at.mateball.domain.group.core.calculator.MatchingTarget;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;
import at.mateball.domain.group.infrastructure.dto.MemberMatchCountDto;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.GroupMatchSummaryRes;
import at.mateball.domain.groupmember.api.dto.MatchRequestDetailRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.groupmember.infrastructure.GroupMemberQueryRepository;
import at.mateball.domain.groupmember.infrastructure.MatchRequestDetailQueryDto;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberV3Service {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupMemberQueryRepository groupMemberQueryRepository;
    private final MatchingScoreCalculator matchingScoreCalculator;
    private final FileStorage fileStorage;

    public void updateMemberStatusMatched(Long userId, Long groupId) {
        groupMemberRepository.updateStatusAndParticipant(userId, groupId, GroupMemberStatus.MATCHED.getValue());
    }

    public void updateLeaderStatusPending(Long userId, Long groupId) {
        groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.PENDING_REQUEST.getValue());
    }

    public void updateMemberStatusFailed(Long userId, Long groupId) {
        groupMemberRepository.updateMemberStatus(userId, groupId, GroupMemberStatus.MATCH_FAILED.getValue());
    }

    public GroupMatchSummaryRes getMatchSummary(Long groupId) {
        return groupMemberRepository.getMatchSummary(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.REQUEST_NOT_FOUND));
    }
    public List<MatchRequestDetailRes> getMatchRequestDetails(Long userId, Long matchId) {
        validateAccessibleRequestMatch(userId, matchId);

        LoginUserMatchRequirementDto loginRequirement = groupMemberQueryRepository.findLoginUserMatchRequirement(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND));

        MatchingTarget loginUserTarget = loginRequirement.toTarget(userId);

        List<MatchRequestDetailQueryDto> requestMembers =
                groupMemberQueryRepository.findMatchRequestDetailMembers(matchId);

        if (requestMembers.isEmpty()) {
            return List.of();
        }

        Map<Long, Integer> avgGameMap = buildAvgGameMap(requestMembers);

        return requestMembers.stream()
                .map(member -> {
                    Long matchRate = calculateMatchRate(loginUserTarget, member);
                    String imageUrl = fileStorage.getImageUrl(member.profileImageKey());
                    Integer avgGame = avgGameMap.getOrDefault(member.memberId(), 0);

                    return MatchRequestDetailRes.of(member, avgGame, matchRate, imageUrl);
                })
                .toList();
    }

    private void validateAccessibleRequestMatch(Long userId, Long matchId) {
        boolean isAccessible = groupMemberQueryRepository.existsAccessibleRequestMatch(userId, matchId);

        if (!isAccessible) {
            throw new BusinessException(BusinessErrorCode.REQUEST_NOT_FOUND);
        }
    }

    private Map<Long, Integer> buildAvgGameMap(List<MatchRequestDetailQueryDto> requestMembers) {
        List<Long> memberIds = requestMembers.stream()
                .map(MatchRequestDetailQueryDto::memberId)
                .toList();

        return groupMemberQueryRepository.countGroupMembersByUserIds(memberIds).stream()
                .collect(Collectors.toMap(
                        MemberMatchCountDto::memberId,
                        MemberMatchCountDto::matchCount
                ));
    }

    private Long calculateMatchRate(
            MatchingTarget loginUserTarget,
            MatchRequestDetailQueryDto member
    ) {
        return (long) matchingScoreCalculator.calculate(
                loginUserTarget,
                member.toMatchingTarget()
        );
    }
}
