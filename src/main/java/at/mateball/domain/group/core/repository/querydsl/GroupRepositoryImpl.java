package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.api.dto.base.GroupCreateBaseRes;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.api.dto.base.DirectCreateBaseRes;
import at.mateball.domain.group.api.dto.base.DirectGetBaseRes;
import at.mateball.domain.group.api.dto.base.GroupGetBaseRes;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static at.mateball.domain.user.core.QUser.user;

public class GroupRepositoryImpl implements GroupRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public GroupRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public DirectCreateRes findDirectCreateResults(Long userId, Long matchId) {
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation game = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        DirectCreateBaseRes baseRes = queryFactory
                .select(Projections.constructor(DirectCreateBaseRes.class,
                        group.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        game.awayTeamName,
                        game.homeTeamName,
                        game.stadiumName,
                        game.gameDate,
                        user.imgUrl
                ))
                .from(group)
                .join(user).on(group.leader.eq(user))
                .join(game).on(group.gameInformation.eq(game))
                .join(matchRequirement).on(matchRequirement.user.eq(user))
                .where(
                        group.leader.id.eq(userId),
                        group.id.eq(matchId),
                        group.isGroup.isFalse()
                )
                .fetchOne();

        return baseRes != null ? DirectCreateRes.from(baseRes) : null;
    }

    @Override
    public List<DirectGetBaseRes> findDirectGroupsByDate(Long userId,LocalDate date) {
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation game = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;
        QGroupMember groupMember = QGroupMember.groupMember;
        return queryFactory
                .select(Projections.constructor(DirectGetBaseRes.class,
                        group.id,
                        user.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        game.awayTeamName,
                        game.homeTeamName,
                        game.stadiumName,
                        game.gameDate,
                        Expressions.nullExpression(Integer.class),
                        user.imgUrl
                ))
                .from(group)
                .join(user).on(group.leader.eq(user))
                .join(game).on(group.gameInformation.eq(game))
                .join(matchRequirement).on(matchRequirement.user.eq(user))
                .where(
                        group.isGroup.isFalse(),
                        group.leader.id.ne(userId),
                        game.gameDate.eq(date),
                        JPAExpressions
                                .selectOne()
                                .from(groupMember)
                                .where(
                                        groupMember.group.id.eq(group.id),
                                        groupMember.status.ne(1)
                                )
                                .notExists()

                )
                .orderBy(game.gameDate.asc())
                .fetch();
    }

    @Override
    public Optional<GroupCreateRes> findGroupCreateRes(Long userId, Long matchId) {
        QGroup group = QGroup.group;
        QUser leader = user;
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QGroupMember groupMember = QGroupMember.groupMember;
        QUser member = new QUser("member");

        GroupCreateBaseRes base = queryFactory
                .select(Projections.constructor(GroupCreateBaseRes.class,
                        group.id,
                        leader.nickname,
                        gameInformation.awayTeamName,
                        gameInformation.homeTeamName,
                        gameInformation.stadiumName,
                        gameInformation.gameDate))
                .from(group)
                .join(leader).on(group.leader.eq(leader))
                .where(
                        group.id.eq(matchId),
                        group.isGroup.isTrue(),
                        group.leader.id.eq(userId)
                )
                .fetchOne();

        if (base == null) {
            return Optional.empty();
        }

        int count = Optional.ofNullable(queryFactory
                .select(groupMember.count().intValue())
                .from(groupMember)
                .where(
                        groupMember.group.id.eq(matchId),
                        groupMember.isParticipant.isTrue()
                )
                .fetchOne()).orElse(0);

        List<String> imgUrls = queryFactory
                .select(member.imgUrl)
                .from(groupMember)
                .join(member).on(groupMember.user.eq(member))
                .where(
                        groupMember.group.id.eq(matchId),
                        groupMember.isParticipant.isTrue()
                )
                .fetch();

        return Optional.of(GroupCreateRes.from(base, count, imgUrls));
    }

    @Override
    public List<GroupGetBaseRes> findGroupsWithBaseInfo(LocalDate date) {
        QGroup group = QGroup.group;
        QUser leader = user;
        QGameInformation game = QGameInformation.gameInformation;
        QGroupMember groupMember = QGroupMember.groupMember;

        return queryFactory
                .select(Projections.constructor(GroupGetBaseRes.class,
                        group.id,
                        leader.nickname,
                        game.awayTeamName,
                        game.homeTeamName,
                        game.stadiumName,
                        game.gameDate
                ))
                .from(group)
                .join(leader).on(group.leader.eq(leader))
                .join(game).on(group.gameInformation.eq(game))
                .where(
                        group.isGroup.isTrue(),
                        game.gameDate.eq(date),
                        JPAExpressions
                                .selectOne()
                                .from(groupMember)
                                .where(
                                        groupMember.group.id.eq(group.id),
                                        groupMember.status.ne(1),
                                        groupMember.isParticipant.isTrue()
                                )
                                .notExists()
                )
                .fetch();
    }

    @Override
    public void updateGroupStatus(Long groupId, int status) {
        queryFactory
                .update(QGroup.group)
                .set(QGroup.group.status, status)
                .where(QGroup.group.id.eq(groupId))
                .execute();
    }
}
