package at.mateball.domain.groupmember.core.service;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.DirectStatusListRes;
import at.mateball.domain.groupmember.api.dto.DirectStatusRes;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
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

        List<DirectStatusRes> mapped = baseResList.stream()
                .map(DirectStatusRes::from)
                .toList();

        return new DirectStatusListRes(mapped);
    }
}
