package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.api.dto.GroupBaseDto;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.api.dto.base.DirectBaseRes;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        Tuple tuple = queryFactory
                .select(
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
                )
                .from(group)
                .join(user).on(group.leader.eq(user))
                .join(game).on(group.gameInformation.eq(game))
                .join(matchRequirement).on(matchRequirement.user.eq(user))
                .where(
                        user.id.eq(userId),
                        group.id.eq(matchId)
                )
                .fetchOne();

        return tuple != null ? DirectCreateRes.from(tuple) : null;
    }

    @Override
    public List<DirectBaseRes> findDirectGroupsByDate(LocalDate date) {
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation game = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        int currentYear = LocalDate.now().getYear();

        return queryFactory
                .select(Projections.constructor(DirectBaseRes.class,
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
                        Expressions.nullExpression(Integer.class),
                        user.imgUrl
                ))
                .from(group)
                .join(user).on(group.leader.eq(user))
                .join(game).on(group.gameInformation.eq(game))
                .join(matchRequirement).on(matchRequirement.user.eq(user))
                .where(
                        game.gameDate.eq(date),
                        group.isGroup.isFalse()
                )
                .orderBy(game.gameDate.asc())
                .fetch();
    }

    @Override
    public Optional<GroupCreateRes> findGroupCreateRes(Long matchId) {
        QGroup group = QGroup.group;
        QUser leader = QUser.user;
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QGroupMember groupMember = QGroupMember.groupMember;
        QUser member = new QUser("member");

        GroupBaseDto base = queryFactory
                .select(Projections.constructor(GroupBaseDto.class,
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
                        group.isGroup.isTrue()
                )
                .fetchOne();

        if (base == null) {
            return Optional.empty();
        }

        int count = Optional.ofNullable(queryFactory
                .select(groupMember.count().intValue())
                .from(groupMember)
                .where(groupMember.group.id.eq(matchId))
                .fetchOne()).orElse(0);

        List<String> imgUrls = queryFactory
                .select(member.imgUrl)
                .from(groupMember)
                .join(member).on(groupMember.user.eq(member))
                .where(groupMember.group.id.eq(matchId))
                .fetch();

        return Optional.of(GroupCreateRes.from(base, count, imgUrls));
    }
}
