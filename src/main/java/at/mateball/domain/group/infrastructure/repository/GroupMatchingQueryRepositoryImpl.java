package at.mateball.domain.group.infrastructure.repository;

import at.mateball.domain.group.infrastructure.dto.GroupMemberMatchingQueryDto;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static at.mateball.domain.group.core.QGroup.group;
import static at.mateball.domain.groupmember.core.QGroupMember.groupMember;

@Repository
@RequiredArgsConstructor
public class GroupMatchingQueryRepositoryImpl implements GroupMatchingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupMemberMatchingQueryDto> findGroupMembersForMatching(Long loginUserId, Long groupId) {
        QUser memberUser = new QUser("memberUser");
        QMatchRequirement memberRequirement = new QMatchRequirement("memberRequirement");
        QMatchRequirement loginUserRequirement = new QMatchRequirement("loginUserRequirement");

        return queryFactory
                .select(Projections.constructor(
                        GroupMemberMatchingQueryDto.class,
                        memberUser.id,
                        memberRequirement.team,
                        memberRequirement.teamAllowed,
                        memberRequirement.style,
                        loginUserRequirement.team,
                        loginUserRequirement.teamAllowed,
                        loginUserRequirement.style
                ))
                .from(groupMember)
                .join(groupMember.group, group)
                .join(groupMember.user, memberUser)
                .leftJoin(memberRequirement).on(memberRequirement.user.id.eq(memberUser.id))
                .leftJoin(loginUserRequirement).on(loginUserRequirement.user.id.eq(loginUserId))
                .where(
                        group.id.eq(groupId),
                        memberUser.id.ne(loginUserId)
                )
                .fetch();
    }
}