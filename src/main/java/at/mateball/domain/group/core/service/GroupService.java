package at.mateball.domain.group.core.service;

import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public DirectCreateRes getDirectMatching(Long userId, Long matchId) {
        return groupRepository.findDirectCreateResults(userId, matchId);
    }
}
