package at.mateball.domain.groupmember.core.repository;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.groupmember.core.GroupMemberStatus;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.user.core.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    QGroupMember groupMember = QGroupMember.groupMember;

    public GroupMemberRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public void createGroupMember(Long userId, Long matchId) {
        User user = entityManager.getReference(User.class, userId);
        Group group = entityManager.getReference(Group.class, matchId);

        GroupMember groupMember = new GroupMember(user, group, false, GroupMemberStatus.AWAITING_APPROVAL.getValue());

        entityManager.persist(groupMember);
    }

    @Override
    public long countMatchingRequests(Long userId, boolean isGroup) {
        Long result = queryFactory
                .select(groupMember.count())
                .from(groupMember)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.group.isGroup.eq(isGroup),
                        groupMember.status.in(
                                GroupMemberStatus.PENDING_REQUEST.getValue(),
                                GroupMemberStatus.NEW_REQUEST.getValue(),
                                GroupMemberStatus.AWAITING_APPROVAL.getValue()
                        )
                )
                .fetchOne();

        return result != null ? result : 0L;
    }

    @Override
    public boolean isPendingRequestExists(Long matchId, List<Integer> statuses) {
        Integer result = queryFactory
                .selectOne()
                .from(groupMember)
                .where(
                        groupMember.group.id.eq(matchId),
                        groupMember.status.in(statuses)
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public void updateLeaderStatus(Long userId, Long matchId, int status) {
        queryFactory
                .update(groupMember)
                .set(groupMember.status, status)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.group.id.eq(matchId)
                )
                .execute();
    }

    @Override
    public void updateStatusForAllParticipants(Long matchId, int status) {
        queryFactory
                .update(groupMember)
                .set(groupMember.status, status)
                .where(
                        groupMember.group.id.eq(matchId),
                        groupMember.isParticipant.isTrue()
                )
                .execute();
    }
}
