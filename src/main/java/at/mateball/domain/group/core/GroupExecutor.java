package at.mateball.domain.group.core;

import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class GroupExecutor {

    private final EntityManager entityManager;

    public GroupExecutor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Long createGroup(Long userId, Long gameId, boolean isGroup) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);
        }

        GameInformation game = entityManager.find(GameInformation.class, gameId);
        if (game == null) {
            throw new BusinessException(BusinessErrorCode.GAME_NOT_FOUND);
        }

        Group group = new Group(user, game, LocalDateTime.now(), GroupStatus.PENDING.getValue(), isGroup);
        entityManager.persist(group);

        GroupMember leader = new GroupMember(user, group, true, GroupMemberStatus.PENDING_REQUEST.getValue());
        entityManager.persist(leader);

        return group.getId();
    }
}
