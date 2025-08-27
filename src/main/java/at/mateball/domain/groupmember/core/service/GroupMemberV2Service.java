package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.DirectStatusListRes;
import at.mateball.domain.groupmember.api.dto.DirectStatusResV2;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseResV2;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberV2Service {
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberV2Service(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    public DirectStatusListRes getDirectStatusV2(Long userId, GroupStatus groupStatus) {
        List<DirectStatusBaseResV2> baseResList =
                groupMemberRepository.findDirectMatchingsByUserAndGroupStatusV2(userId, groupStatus.getValue());

        List<DirectStatusResV2> result = baseResList.stream()
                .map(baseRes -> DirectStatusResV2.fromV2(baseRes, userId))
                .toList();

        return new DirectStatusListRes(result);
    }

    public DirectStatusListRes getAllDirectStatusV2(Long userId) {
        List<DirectStatusBaseResV2> baseResList =
                groupMemberRepository.findAllDirectMatchingsByUserV2(userId);

        List<DirectStatusResV2> result = baseResList.stream()
                .map(baseRes -> DirectStatusResV2.fromV2(baseRes, userId))
                .toList();

        return new DirectStatusListRes(result);
    }
}
