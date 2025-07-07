package at.mateball.domain.matchrequirement.core.repository.querydsl;

import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.user.core.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class MatchRequirementRepositoryImpl implements MatchRequirementRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public MatchRequirementRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public List<MatchingScoreDto> findMatchingUsers(Long userId) {
        QUser userB = new QUser("userB");
        QMatchRequirement requirementB = new QMatchRequirement("requirementB");

        User user = entityManager.find(User.class, userId);
        MatchRequirement reqAEntity = entityManager.createQuery(
                        "SELECT r FROM MatchRequirement r WHERE r.user.id = :id", MatchRequirement.class)
                .setParameter("id", userId)
                .getSingleResult();

        int birthYearA = user.getBirthYear();
        int teamA = reqAEntity.getTeam();
        int allowA = reqAEntity.getTeamAllowed();
        int styleA = reqAEntity.getStyle();
        int prefA = reqAEntity.getGenderPreference();
        String genderA = user.getGender();

        int thisYear = LocalDateTime.now().getYear();

        return queryFactory.select(Projections.constructor(MatchingScoreDto.class,
                        userB.id,
                        Expressions.numberTemplate(Integer.class,
                                "CASE WHEN {0} = 2 AND {1} = 2 THEN 40 " +
                                        "WHEN ({0} = 1 OR {1} = 1) AND {2} = {3} AND {2} != 11 THEN 40 " +
                                        "WHEN ({2} = 11 OR {3} = 11) AND {0} = 2 AND {1} = 2 THEN 40 ELSE 0 END",
                                Expressions.constant(allowA), requirementB.teamAllowed,
                                Expressions.constant(teamA), requirementB.team),

                        Expressions.numberTemplate(Integer.class,
                                "CASE WHEN {0} = 3 AND {1} = 3 THEN 35 " +
                                        "WHEN ({0} = 3 AND ({2} = 'M' AND {3} = 1 OR {2} = 'F' AND {3} = 2)) THEN 35 " +
                                        "WHEN ({1} = 3 AND ({4} = 'M' AND {5} = 1 OR {4} = 'F' AND {5} = 2)) THEN 35 " +
                                        "WHEN ({3} = 1 AND {2} = 'M' AND {5} = 2 AND {4} = 'F') THEN 35 " +
                                        "WHEN ({3} = 2 AND {2} = 'F' AND {5} = 1 AND {4} = 'M') THEN 35 " +
                                        "WHEN ({3} = 1 AND {2} = 'M' AND {5} = 1 AND {4} = 'M') THEN 35 " +
                                        "WHEN ({3} = 2 AND {2} = 'F' AND {5} = 2 AND {4} = 'F') THEN 35 ELSE 0 END",
                                Expressions.constant(prefA), requirementB.genderPreference,
                                Expressions.constant(genderA), requirementB.genderPreference,
                                userB.gender, Expressions.constant(prefA)),

                        Expressions.numberTemplate(Integer.class,
                                "CASE WHEN {0} = {1} THEN 25 " +
                                        "WHEN ({0} = 1 AND {1} = 3) OR ({0} = 3 AND {1} = 1) THEN 20 ELSE 10 END",
                                Expressions.constant(styleA), requirementB.style)
                ))
                .from(userB)
                .join(requirementB.user, userB)
                .where(userB.id.ne(userId)
                        .and(Expressions.numberTemplate(Integer.class,
                                "ABS(({0} - {1} + 1) - ({2} - {3} + 1))",
                                Expressions.constant(thisYear), userB.birthYear,
                                Expressions.constant(thisYear), Expressions.constant(birthYearA)
                        ).loe(5)))
                .fetch();
    }
}
