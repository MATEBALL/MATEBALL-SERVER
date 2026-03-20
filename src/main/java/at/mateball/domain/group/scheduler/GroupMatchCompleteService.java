package at.mateball.domain.group.scheduler;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupMatchCompleteService {
    private static final int MIN_PARTICIPANT_FOR_COMPLETE = 2;

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void validateCompleteStatus(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        int memberCount = groupMemberRepository.countGroupMember(groupId).count();

        if ((group.isGroup() && memberCount >= MIN_PARTICIPANT_FOR_COMPLETE)) {
            groupMemberRepository.updateAllStatusComplete(groupId);
        }
    }
}
