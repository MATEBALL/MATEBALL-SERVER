package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.infrastructure.dto.*;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
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
}