package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.DirectStatusListRes;
import at.mateball.domain.groupmember.api.dto.DirectStatusResV2;
import at.mateball.domain.groupmember.api.dto.GroupStatusListRes;
import at.mateball.domain.groupmember.api.dto.GroupStatusResV2;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseResV2;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseResV2;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public GroupStatusListRes getAllGroupStatusV2(Long userId) {
        List<GroupStatusBaseResV2> baseResList = groupMemberRepository.findGroupMatchingsByUserV2(userId);

        Map<Long, Integer> countMap = groupMemberRepository.findGroupMemberCountMap(
                baseResList.stream().map(GroupStatusBaseResV2::id).toList()
        );
        Map<Long, List<String>> imgMap = groupMemberRepository.findGroupMemberImgMap(
                baseResList.stream().map(GroupStatusBaseResV2::id).toList()
        );

        List<GroupStatusResV2> result = baseResList.stream()
                .map(base -> GroupStatusResV2.from(
                        base,
                        countMap.getOrDefault(base.id(), 0) - 1,
                        imgMap.getOrDefault(base.id(), List.of()),
                        userId
                ))
                .toList();

        return new GroupStatusListRes(result);
    }

    public GroupStatusListRes getGroupStatusV2(Long userId, GroupStatus status) {
        List<GroupStatusBaseResV2> baseResList = groupMemberRepository.findGroupMatchingsByUserAndStatusV2(userId, status.getValue());

        Map<Long, Integer> countMap = groupMemberRepository.findGroupMemberCountMap(
                baseResList.stream().map(GroupStatusBaseResV2::id).toList()
        );
        Map<Long, List<String>> imgMap = groupMemberRepository.findGroupMemberImgMap(
                baseResList.stream().map(GroupStatusBaseResV2::id).toList()
        );

        List<GroupStatusResV2> result = baseResList.stream()
                .map(base -> GroupStatusResV2.from(
                        base,
                        countMap.getOrDefault(base.id(), 0) - 1,
                        imgMap.getOrDefault(base.id(), List.of()),
                        userId
                ))
                .toList();

        return new GroupStatusListRes(result);
    }
}
