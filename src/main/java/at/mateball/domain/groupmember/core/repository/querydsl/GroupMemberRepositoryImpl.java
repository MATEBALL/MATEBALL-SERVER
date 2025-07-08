package at.mateball.domain.groupmember.core.repository.querydsl;

import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.user.core.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public GroupMemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds) {
        QGroupMember member = QGroupMember.groupMember;
        QUser user = QUser.user;

        if (groupIds.isEmpty()) return Map.of();

        return queryFactory
                .select(member.group.id, member.count())
                .from(member)
                .where(member.group.id.in(groupIds))
                .groupBy(member.group.id)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, Long.class),
                        tuple -> tuple.get(1, Long.class).intValue() + 1
                ));
    }

    @Override
    public Map<Long, List<String>> findGroupMemberImgMap(List<Long> groupIds) {
        QGroupMember member = QGroupMember.groupMember;
        QUser user = QUser.user;

        if (groupIds.isEmpty()) return Map.of();

        return queryFactory
                .select(member.group.id, user.imgUrl)
                .from(member)
                .join(user).on(member.user.eq(user))
                .where(member.group.id.in(groupIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, Long.class),
                        Collectors.mapping(t -> t.get(1, String.class), Collectors.toList())
                ));
    }
}
