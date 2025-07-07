package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.QGroup;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

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
        QGameInformation gameInformation = QGameInformation.gameInformation;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        List<Tuple> tuples = queryFactory
                .select(
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
                        user.imgUrl
                )
                .from(group)
                .join(user).on(group.id.eq(user.id))
                .join(gameInformation).on(group.id.eq(gameInformation.id))
                .join(matchRequirement).on(user.id.eq(matchRequirement.user.id))
                .where(user.id.eq(userId))
                .fetch();

        return tuples.stream()
                .map(DirectCreateRes::from)
                .toList();
    }

}
