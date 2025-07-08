package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.api.dto.DirectGetRes;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

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
    public List<DirectGetRes> findDirectGroupsByDate(LocalDate date) {
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QGameInformation game = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        int currentYear = LocalDate.now().getYear();

        return queryFactory
                .select(Projections.constructor(DirectGetRes.class,
                        group.id,
                        user.nickname,
                        Expressions.stringTemplate("CONCAT({0} - {1} + 1, '세')", currentYear, user.birthYear),
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
}
