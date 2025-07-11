package at.mateball.domain.groupmember.core.repository.querydsl;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.api.dto.GroupMemberCountRes;
import at.mateball.domain.groupmember.api.dto.base.DetailMatchingBaseRes;
import at.mateball.domain.groupmember.api.dto.base.DirectStatusBaseRes;
import at.mateball.domain.groupmember.api.dto.base.GroupStatusBaseRes;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.user.core.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    QGroupMember groupMember = QGroupMember.groupMember;
    QGroup group = QGroup.group;

    public GroupMemberRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }


    @Override
    public Map<Long, Integer> findGroupMemberCountMap(List<Long> groupIds) {
        QGroupMember member = QGroupMember.groupMember;
        QUser user = QUser.user;

        if (groupIds.isEmpty()) return Map.of();

        return queryFactory
                .select(member.group.id, member.count())
                .from(member)
                .where(
                        member.group.id.in(groupIds),
                        member.isParticipant.isTrue()
                )
                .groupBy(member.group.id)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, Long.class),
                        tuple -> tuple.get(1, Long.class).intValue()
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
                .where(
                        member.group.id.in(groupIds),
                        member.isParticipant.isTrue()
                )
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, Long.class),
                        Collectors.mapping(t -> t.get(1, String.class), Collectors.toList())
                ));
    }

    @Override
    public List<DirectStatusBaseRes> findDirectMatchingsByUserAndGroupStatus(Long userId, int groupStatus) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        return queryFactory
                .select(Projections.constructor(DirectStatusBaseRes.class,
                        group.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        gameInformation.awayTeamName,
                        gameInformation.homeTeamName,
                        gameInformation.stadiumName,
                        gameInformation.gameDate,
                        groupMember.status,
                        user.imgUrl
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(groupMember.user, user)
                .join(group.gameInformation, gameInformation)
                .join(matchRequirement).on(matchRequirement.user.id.eq(user.id))
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.isParticipant.isTrue(),
                        group.isGroup.isFalse(),
                        group.status.eq(groupStatus)
                )
                .fetch();
    }

    @Override
    public List<DirectStatusBaseRes> findAllDirectMatchingsByUser(Long userId) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        return queryFactory
                .select(Projections.constructor(DirectStatusBaseRes.class,
                        group.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        gameInformation.awayTeamName,
                        gameInformation.homeTeamName,
                        gameInformation.stadiumName,
                        gameInformation.gameDate,
                        groupMember.status,
                        user.imgUrl
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(groupMember.user, user)
                .join(group.gameInformation, gameInformation)
                .join(matchRequirement).on(matchRequirement.user.id.eq(user.id))
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.isParticipant.isTrue(),
                        group.isGroup.isFalse()
                )
                .fetch();
    }

    @Override
    public List<GroupStatusBaseRes> findGroupMatchingsByUser(Long userId) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;

        return queryFactory
                .select(Projections.constructor(GroupStatusBaseRes.class,
                        group.id,
                        user.nickname,
                        gameInformation.awayTeamName,
                        gameInformation.homeTeamName,
                        gameInformation.stadiumName,
                        gameInformation.gameDate,
                        groupMember.status
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(groupMember.user, user)
                .join(group.gameInformation, gameInformation)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.isParticipant.isTrue(),
                        group.isGroup.isTrue()
                )
                .fetch();
    }

    @Override
    public List<GroupStatusBaseRes> findGroupMatchingsByUserAndStatus(Long userId, int groupStatus) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;

        return queryFactory
                .select(Projections.constructor(GroupStatusBaseRes.class,
                        group.id,
                        user.nickname,
                        gameInformation.awayTeamName,
                        gameInformation.homeTeamName,
                        gameInformation.stadiumName,
                        gameInformation.gameDate,
                        groupMember.status
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(groupMember.user, user)
                .join(group.gameInformation, gameInformation)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.isParticipant.isTrue(),
                        group.isGroup.isTrue(),
                        group.status.eq(groupStatus)
                )
                .fetch();
    }

    @Override
    public GroupMemberCountRes countGroupMember(Long groupId) {
        QGroupMember groupMember = QGroupMember.groupMember;

        Long count = queryFactory
                .select(groupMember.count())
                .from(groupMember)
                .where(groupMember.group.id.eq(groupId), groupMember.isParticipant.isTrue())
                .fetchOne();

        return new GroupMemberCountRes(count != null ? count.intValue() : 0);
    }

    @Override
    public List<DetailMatchingBaseRes> findGroupMatesByMatchId(Long userId, Long matchId) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;
        QGroup group = QGroup.group;
        QGameInformation game = QGameInformation.gameInformation;

        return queryFactory
                .select(Projections.constructor(DetailMatchingBaseRes.class,
                        group.id,
                        user.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        user.introduction,
                        game.awayTeamName,
                        game.homeTeamName,
                        game.stadiumName,
                        game.gameDate,
                        user.imgUrl
                ))
                .from(groupMember)
                .join(groupMember.user, user)
                .join(groupMember.group, group)
                .join(group.gameInformation, game)
                .join(matchRequirement).on(matchRequirement.user.id.eq(user.id))
                .where(
                        group.id.eq(matchId),
                        groupMember.isParticipant.isTrue(),
                        groupMember.status.eq(1),
                        user.id.ne(userId)
                )
                .fetch();
    }

    @Override
    public boolean existsRequest(Long userId, Long groupId) {
        Integer result = queryFactory
                .selectOne()
                .from(groupMember)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.group.id.eq(groupId),
                        groupMember.status.eq(GroupMemberStatus.AWAITING_APPROVAL.getValue())
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public boolean isPendingRequestExists(Long matchId, List<Integer> status) {
        Integer result = queryFactory
                .selectOne()
                .from(groupMember)
                .where(
                        groupMember.group.id.eq(matchId),
                        groupMember.status.in(status)
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public boolean hasNonFailedRequestOnSameDate(Long userId, LocalDate date) {
        Integer result = queryFactory
                .selectOne()
                .from(groupMember)
                .join(groupMember.group, group)
                .join(group.gameInformation, QGameInformation.gameInformation)
                .where(
                        groupMember.user.id.eq(userId),
                        QGameInformation.gameInformation.gameDate.eq(date),
                        groupMember.status.ne(GroupMemberStatus.MATCH_FAILED.getValue())
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public boolean hasPreviousFailedRequest(Long userId, Long matchId, GroupMemberStatus status) {
        return queryFactory
                .selectOne()
                .from(groupMember)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.group.id.eq(matchId),
                        groupMember.status.eq(status.getValue())
                )
                .fetchFirst() != null;
    }

    @Override
    public long countMatchingRequests(Long userId, boolean isGroup) {
        Long result = queryFactory
                .select(groupMember.count())
                .from(groupMember)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.group.isGroup.eq(isGroup),
                        group.status.eq(GroupStatus.PENDING.getValue())
                )
                .fetchOne();

        return result != null ? result : 0L;
    }

    @Override
    public void createGroupMember(Long userId, Long matchId) {
        User user = entityManager.getReference(User.class, userId);
        Group group = entityManager.getReference(Group.class, matchId);

        GroupMember groupMember = new GroupMember(user, group, false, GroupMemberStatus.AWAITING_APPROVAL.getValue());

        entityManager.persist(groupMember);
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

    @Override
    public boolean existsParticipantInGroupWithGroupCheck(Long groupId, Long userId) {
        Integer result = queryFactory
                .selectOne()
                .from(groupMember)
                .join(groupMember.group, group)
                .where(
                        group.id.eq(groupId),
                        groupMember.user.id.eq(userId),
                        groupMember.isParticipant.isTrue()
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public void updateAllGroupMembersStatus(Long groupId, int status) {
        queryFactory
                .update(groupMember)
                .set(groupMember.status, status)
                .where(groupMember.group.id.eq(groupId))
                .execute();
    }
}
