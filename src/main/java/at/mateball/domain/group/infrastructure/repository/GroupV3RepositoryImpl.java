package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.infrastructure.dto.GameInfoQueryDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchCandidateFlatDto;
import at.mateball.domain.group.infrastructure.dto.GroupMatchMeberQueryDto;
import at.mateball.domain.group.infrastructure.dto.LoginUserMatchRequirementDto;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.Projections;
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
    public List<GroupMatchMeberQueryDto> findMatchMembersByMatchId(Long matchId) {
        QGroupMember groupMember = new QGroupMember("groupMember");
        QUser memberUser = new QUser("memberUser");
        QMatchRequirement memberRequirement = new QMatchRequirement("memberRequirement");

        return queryFactory
                .select(Projections.constructor(
                        GroupMatchMeberQueryDto.class,
                        memberUser.id,
                        memberUser.nickname,
                        memberRequirement.team,
                        memberRequirement.style,
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
}