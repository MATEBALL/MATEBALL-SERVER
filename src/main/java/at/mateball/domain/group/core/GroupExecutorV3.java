package at.mateball.domain.group.core;

import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.chatting.core.service.ChattingV2Service;
import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static at.mateball.exception.code.BusinessErrorCode.EXCEED_MATCHING_LIMIT;

@Component
public class GroupExecutorV3 {

    private final EntityManager entityManager;
    private final ChattingV2Service chattingV2Service;

    public GroupExecutorV3(EntityManager entityManager, ChattingV2Service chattingV2Service) {
        this.entityManager = entityManager;
        this.chattingV2Service = chattingV2Service;
    }

    @Transactional
    public Long createGroup(Long userId, Long gameId, boolean isGroup) {

        try {
            User user = entityManager.find(User.class, userId);

            if (user == null) {
                throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);
            }

            GameInformation game = entityManager.find(GameInformation.class, gameId);

            Chatting chatting = chattingV2Service.assignChatting();
            if (chatting == null) {
                throw new BusinessException(BusinessErrorCode.CHATTING_NOT_FOUND);
            }

            Group group = Group.create(user, game, isGroup);
            group.assignChatting(chatting);
            entityManager.persist(group);
            entityManager.flush();

            GroupMember leader = GroupMember.leader(user, group, GroupMemberStatus.PENDING_REQUEST.getValue());
            entityManager.persist(leader);

            return group.getId();

        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(EXCEED_MATCHING_LIMIT);
        }
    }
}
