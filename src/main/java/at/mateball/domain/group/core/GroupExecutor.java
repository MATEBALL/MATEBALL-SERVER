package at.mateball.domain.group.core;

import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.user.core.User;
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
        User user = entityManager.getReference(User.class, userId);
        GameInformation game = entityManager.getReference(GameInformation.class, gameId);

        Group group = new Group(user, game, LocalDateTime.now(), GroupStatus.PENDING.getValue(), isGroup);
        entityManager.persist(group);

        GroupMember leader = new GroupMember(user, group, true, GroupMemberStatus.PENDING_REQUEST.getValue());
        entityManager.persist(leader);

        return group.getId();
    }
}
