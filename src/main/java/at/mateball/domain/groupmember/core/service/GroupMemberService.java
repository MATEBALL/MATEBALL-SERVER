package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.group.api.dto.DirectGetListRes;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    public DirectGetListRes getDirectStatus(Long userId, GroupStatus groupStatus) {
        return groupMemberRepository.findDirectMatchingsByUserAndGroupStatus(userId, groupStatus);
    }
}
