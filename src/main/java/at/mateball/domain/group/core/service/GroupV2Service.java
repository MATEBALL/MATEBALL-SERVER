package at.mateball.domain.group.core.service;

import at.mateball.domain.chatting.api.dto.response.ChattingRes;
import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service

public class GroupV2Service {
    private final GroupRepository groupRepository;

    public GroupV2Service(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public ChattingRes getChattingUrl(Long userId, @NotNull Long matchId) {
        Group group = groupRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND));

        if (GroupStatus.from(group.getStatus()) != GroupStatus.COMPLETED) {
            throw new BusinessException(BusinessErrorCode.INVALID_GROUP_STATUS);
        }

        Chatting chatting = group.getChatting();
        return new ChattingRes(chatting.getChattingUrl());
    }
}
