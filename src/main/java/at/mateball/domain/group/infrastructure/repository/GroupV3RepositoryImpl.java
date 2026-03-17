package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.api.dto.MatchValidationRes;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static at.mateball.domain.gameinformation.core.QGameInformation.gameInformation;
import static at.mateball.domain.group.core.QGroup.group;

@Repository
@RequiredArgsConstructor
public class GroupV3RepositoryImpl implements GroupV3RepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GameInfoQueryDto> findGameInfoByGameId(Long gameId) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                GameInfoQueryDto.class,
                                gameInformation.awayTeamName,
                                gameInformation.homeTeamName,
                                gameInformation.gameDate,
                                gameInformation.stadiumName
                        ))
                        .from(gameInformation)
                        .where(gameInformation.id.eq(gameId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<LoginUserMatchRequirementDto> findLoginUserMatchRequirement(Long userId) {
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                LoginUserMatchRequirementDto.class,
                                matchRequirement.team,
                                matchRequirement.teamAllowed,
                                matchRequirement.style
                        ))
                        .from(matchRequirement)
                        .where(matchRequirement.user.id.eq(userId))
                        .fetchOne()
        );
    }

    @Override
    public List<GroupMatchCandidateFlatDto> findMatchCandidatesByGameId(Long loginUserId, Long gameId) {
        QUser leaderUser = new QUser("leaderUser");
        QUser memberUser = new QUser("memberUser");
        QGroupMember member = new QGroupMember("member");
        QMatchRequirement memberRequirement = new QMatchRequirement("memberRequirement");

        return queryFactory
                .select(Projections.constructor(
                        GroupMatchCandidateFlatDto.class,
                        group.id,
                        leaderUser.id,
                        leaderUser.nickname,
                        group.isGroup,

                        memberUser.id,
                        memberUser.profileImageKey,
                        memberRequirement.team,
                        memberRequirement.teamAllowed,
                        memberRequirement.style
                ))
                .from(group)
                .join(group.leader, leaderUser)
                .join(member).on(member.group.id.eq(group.id))
                .join(member.user, memberUser)
                .leftJoin(memberRequirement).on(memberRequirement.user.id.eq(memberUser.id))
                .where(
                        group.gameInformation.id.eq(gameId),
                        group.status.eq(GroupStatus.PENDING.getValue()),
                        member.status.ne(GroupMemberStatus.MATCH_FAILED.getValue())
                )
                .orderBy(group.id.asc(), member.id.asc())
                .fetch();
    }

    @Override
    public List<GroupMatchImageQueryDto> findMatchImagesByGameId(Long gameId) {
        QGroupMember groupMember = new QGroupMember("groupMember");
        QUser memberUser = new QUser("memberUser");

        return queryFactory
                .select(Projections.constructor(
                        GroupMatchImageQueryDto.class,
                        groupMember.group.id,
                        memberUser.profileImageKey
                ))
                .from(groupMember)
                .join(groupMember.user, memberUser)
                .join(groupMember.group, group)
                .where(
                        group.gameInformation.id.eq(gameId),
                        group.status.eq(GroupStatus.PENDING.getValue()),
                        groupMember.status.ne(GroupMemberStatus.MATCH_FAILED.getValue())
                )
                .orderBy(groupMember.group.id.asc(), groupMember.id.asc())
                .fetch();
    }

    @Override
    public List<CreateGroupQueryDto> findCreateGroupsByUserId(Long userId) {
        QUser leaderUser = new QUser("leaderUser");
        QGroupMember groupMember = new QGroupMember("groupMember");

        NumberExpression<Integer> newRequestFlag = new CaseBuilder()
                .when(groupMember.status.eq(GroupMemberStatus.NEW_REQUEST.getValue()))
                .then(1)
                .otherwise(0);

        return queryFactory
                .select(Projections.constructor(
                        CreateGroupQueryDto.class,
                        group.id,
                        leaderUser.nickname,
                        groupMember.user.id.count().intValue(),
                        group.isGroup,
                        group.gameInformation.awayTeamName,
                        group.gameInformation.homeTeamName,
                        group.gameInformation.gameDate,
                        group.status,
                        newRequestFlag.max().eq(1)
                ))
                .from(group)
                .join(group.leader, leaderUser)
                .join(groupMember).on(groupMember.group.id.eq(group.id))
                .where(group.leader.id.eq(userId))
                .groupBy(
                        group.id,
                        leaderUser.nickname,
                        group.isGroup,
                        group.gameInformation.awayTeamName,
                        group.gameInformation.homeTeamName,
                        group.gameInformation.gameDate,
                        group.status
                )
                .orderBy(group.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CreateGroupImageQueryDto> findCreateGroupImagesByMatchIds(List<Long> matchIds) {
        QGroupMember groupMember = new QGroupMember("groupMember");
        QUser memberUser = new QUser("memberUser");

        if (matchIds == null || matchIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(Projections.constructor(
                        CreateGroupImageQueryDto.class,
                        groupMember.group.id,
                        memberUser.profileImageKey
                ))
                .from(groupMember)
                .join(groupMember.user, memberUser)
                .where(groupMember.group.id.in(matchIds))
                .orderBy(groupMember.group.id.asc(), groupMember.id.asc())
                .fetch();
    }

    @Override
    public Optional<Long> findLeaderIdByMatchId(Long matchId) {
        return Optional.ofNullable(
                queryFactory
                        .select(group.leader.id)
                        .from(group)
                        .where(group.id.eq(matchId))
                        .fetchOne()
        );
    }

    @Override
    public List<GroupMatchMemberQueryDto> findMatchMembersByMatchId(Long matchId) {
        QGroupMember groupMember = new QGroupMember("groupMember");
        QUser memberUser = new QUser("memberUser");
        QMatchRequirement memberRequirement = new QMatchRequirement("memberRequirement");

        return queryFactory
                .select(Projections.constructor(
                        GroupMatchMemberQueryDto.class,
                        memberUser.id,
                        memberUser.gender,
                        memberUser.birthYear,
                        memberUser.nickname,
                        memberUser.introduction,
                        memberRequirement.team,
                        memberRequirement.teamAllowed,
                        memberRequirement.style,
                        memberUser.avgSeason,
                        memberUser.profileImageKey
                ))
                .from(groupMember)
                .join(groupMember.user, memberUser)
                .leftJoin(memberRequirement).on(memberRequirement.user.id.eq(memberUser.id))
                .where(
                        groupMember.group.id.eq(matchId),
                        groupMember.status.ne(GroupMemberStatus.MATCH_FAILED.getValue())
                )
                .orderBy(groupMember.id.asc())
                .fetch();
    }

    @Override
    public List<MemberMatchCountDto> countGroupMembersByUserIds(List<Long> memberIds) {
        QGroupMember groupMember = QGroupMember.groupMember;

        if (memberIds == null || memberIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(Projections.constructor(
                        MemberMatchCountDto.class,
                        groupMember.user.id,
                        groupMember.count().intValue()
                ))
                .from(groupMember)
                .where(groupMember.user.id.in(memberIds))
                .groupBy(groupMember.user.id)
                .fetch();
    }

    @Override
    public List<RequestGroupQueryDto> findRequestGroupsByUserId(Long userId) {
        QGroupMember groupMember = QGroupMember.groupMember;
        QGroupMember leaderGroupMember = new QGroupMember("leaderGroupMember");
        QGroupMember countGroupMember = new QGroupMember("countGroupMember");
        QUser leaderUser = new QUser("leaderUser");

        return queryFactory
                .select(Projections.constructor(
                        RequestGroupQueryDto.class,
                        group.id,
                        leaderUser.nickname,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(countGroupMember.count().intValue())
                                        .from(countGroupMember)
                                        .where(countGroupMember.group.id.eq(group.id)),
                                "count"
                        ),
                        group.isGroup,
                        gameInformation.awayTeamName,
                        gameInformation.homeTeamName,
                        gameInformation.gameDate,
                        groupMember.status
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(group.gameInformation, gameInformation)
                .join(leaderGroupMember).on(
                        leaderGroupMember.group.id.eq(group.id)
                                .and(leaderGroupMember.isLeader.isTrue())
                )
                .join(leaderGroupMember.user, leaderUser)
                .where(
                        groupMember.user.id.eq(userId),
                        groupMember.isLeader.isFalse()
                )
                .orderBy(gameInformation.gameDate.asc(), group.id.asc())
                .fetch();
    }

    @Override
    public List<GroupMatchImageQueryDto> findRequestGroupImagesByMatchIds(List<Long> matchIds) {
        QGroupMember groupMember = QGroupMember.groupMember;

        if (matchIds == null || matchIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(Projections.constructor(
                        GroupMatchImageQueryDto.class,
                        group.id,
                        groupMember.user.profileImageKey
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .where(
                        group.id.in(matchIds),
                        groupMember.isParticipant.isTrue()
                )
                .orderBy(
                        group.id.asc(),
                        groupMember.id.asc()
                )
                .fetch();
    }

    @Override
    public MatchValidationRes getMatchValidationInfo(Long userId, Long gameId) {
        return queryFactory
                .select(Projections.constructor(
                        MatchValidationRes.class,
                        gameInformation.gameDate,
                        group.id.isNotNull()
                ))
                .from(gameInformation)
                .leftJoin(group)
                .on(
                        group.gameInformation.id.eq(gameInformation.id)
                                .and(group.leader.id.eq(userId))
                )
                .where(gameInformation.id.eq(gameId))
                .fetchOne();
    }
}
