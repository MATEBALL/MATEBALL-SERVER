package at.mateball.domain.group.core;

import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class GroupExecutorV3 {

    private final EntityManager entityManager;

    public GroupExecutorV3(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Long createGroup(Long userId, Long gameId, boolean isGroup) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);
        }

        GameInformation game = entityManager.find(GameInformation.class, gameId);

        try {
            Group group = new Group(user, game, LocalDateTime.now(), GroupStatus.PENDING.getValue(), isGroup);
            entityManager.persist(group);
            entityManager.flush();

            GroupMember leader = GroupMember.leader(user, group, GroupMemberStatus.PENDING_REQUEST.getValue());
            entityManager.persist(leader);

            return group.getId();
        } catch (PersistenceException e) {
            throw new BusinessException(BusinessErrorCode.EXCEED_MATCHING_LIMIT);
        }
    }
}
