package at.mateball.domain.groupmember.infrastructure.repository;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.groupmember.infrastructure.dto.MatchRequestDetailQueryDto;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupMemberQueryRepositoryImpl implements GroupMemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public GroupMemberQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public boolean existsAccessibleRequestMatch(Long userId, Long matchId) {
        QGroup group = QGroup.group;
        QGroupMember groupMember = QGroupMember.groupMember;

        Integer result = queryFactory
                .selectOne()
                .from(group)
                .where(
                        group.id.eq(matchId),
                        group.status.eq(1),

                        JPAExpressions
                                .selectOne()
                                .from(groupMember)
                                .where(
                                        groupMember.group.id.eq(matchId),
                                        groupMember.status.eq(3),
                                        groupMember.isParticipant.isFalse()
                                )
                                .exists(),

                        JPAExpressions
                                .selectOne()
                                .from(new QGroupMember("requesterMember"))
                                .where(
                                        new QGroupMember("requesterMember").group.id.eq(matchId),
                                        new QGroupMember("requesterMember").user.id.eq(userId),
                                        new QGroupMember("requesterMember").status.in(1, 2)
                                )
                                .exists()
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public List<MatchRequestDetailQueryDto> findMatchRequestDetailMembers(Long userId, Long matchId) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;
        QGameInformation gameInformation = QGameInformation.gameInformation;

        return queryFactory
                .select(Projections.constructor(
                        MatchRequestDetailQueryDto.class,
                        user.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        user.introduction,
                        user.imgUrl,
                        user.avgSeason
                ))
                .from(groupMember)
                .join(groupMember.user, user)
                .join(groupMember.group, group)
                .join(group.gameInformation, gameInformation)
                .join(matchRequirement).on(matchRequirement.user.id.eq(user.id))
                .where(
                        group.id.eq(matchId),
                        group.status.eq(1),

                        groupMember.status.eq(3),
                        groupMember.isParticipant.isFalse(),

                        JPAExpressions
                                .selectOne()
                                .from(new QGroupMember("requesterMember"))
                                .where(
                                        new QGroupMember("requesterMember").group.id.eq(matchId),
                                        new QGroupMember("requesterMember").user.id.eq(userId),
                                        new QGroupMember("requesterMember").status.in(1, 2)
                                )
                                .exists()
                )
                .orderBy(groupMember.createdAt.asc())
                .fetch();
    }
}
