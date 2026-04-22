package at.mateball.domain.groupmember.infrastructure;

import at.mateball.domain.gameinformation.core.QGameInformation;
import at.mateball.domain.group.core.QGroup;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroupMemberQueryRepositoryImpl implements GroupMemberQueryRepository {

    private static final int GROUP_ACTIVE = 1;
    private static final int REQUEST_MEMBER_STATUS = 3;
    private static final int PENDING_REQUEST_STATUS = 1;
    private static final int NEW_REQUEST_STATUS = 2;

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsAccessibleRequestMatch(Long userId, Long matchId) {
        QGroup group = QGroup.group;
        QGroupMember requestMember = QGroupMember.groupMember;
        QGroupMember requesterMember = new QGroupMember("requesterMember");

        Integer result = queryFactory
                .selectOne()
                .from(group)
                .where(
                        group.id.eq(matchId),
                        group.status.eq(GROUP_ACTIVE),

                        JPAExpressions
                                .selectOne()
                                .from(requestMember)
                                .where(
                                        requestMember.group.id.eq(matchId),
                                        requestMember.status.eq(REQUEST_MEMBER_STATUS),
                                        requestMember.isParticipant.isFalse()
                                )
                                .exists(),

                        JPAExpressions
                                .selectOne()
                                .from(requesterMember)
                                .where(
                                        requesterMember.group.id.eq(matchId),
                                        requesterMember.user.id.eq(userId),
                                        requesterMember.status.in(PENDING_REQUEST_STATUS, NEW_REQUEST_STATUS)
                                )
                                .exists()
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public List<MatchRequestDetailQueryDto> findMatchRequestDetailMembers(Long matchId) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroup group = QGroup.group;
        QUser user = QUser.user;

        return queryFactory
                .select(Projections.constructor(
                        MatchRequestDetailQueryDto.class,
                        user.id,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        user.team,
                        user.style,
                        user.introduction,
                        user.profileImageKey,
                        user.avgSeason
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(groupMember.user, user)
                .where(
                        group.id.eq(matchId),
                        group.status.eq(GROUP_ACTIVE),
                        groupMember.status.eq(REQUEST_MEMBER_STATUS),
                        groupMember.isParticipant.isFalse()
                )
                .orderBy(groupMember.id.asc())
                .fetch();
    }
}
