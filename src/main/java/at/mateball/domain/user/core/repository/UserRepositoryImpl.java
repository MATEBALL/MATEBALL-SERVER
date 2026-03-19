package at.mateball.domain.user.core.repository;

import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.QGroupMember;
import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.api.dto.response.*;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public UserInformationRes findUserInformation(Long userId) {
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        var baseUserInformation = queryFactory
                .select(Projections.constructor(
                        UserInformationBaseRes.class,
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.team,
                        matchRequirement.style,
                        user.introduction,
                        user.imgUrl
                ))
                .from(user, matchRequirement)
                .where(user.id.eq(userId),
                        matchRequirement.user.id.eq(user.id))
                .fetchOne();

        return UserInformationRes.fromBase(baseUserInformation);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        QUser user = QUser.user;

        Integer result = queryFactory
                .selectOne()
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    @Override
    public CheckUserRes fetchUserInfoCheck(Long userId) {
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        Tuple result = queryFactory
                .select(user.nickname,
                        matchRequirement.team,
                        matchRequirement.teamAllowed,
                        matchRequirement.style
                )
                .from(user)
                .leftJoin(matchRequirement).on(matchRequirement.user.id.eq(user.id))
                .where(user.id.eq(userId))
                .fetchOne();

        if (result == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);
        }

        boolean nicknameExists = result.get(user.nickname) != null;

        boolean allConditionsPresent =
                result.get(matchRequirement.team) != null &&
                        result.get(matchRequirement.teamAllowed) != null &&
                        result.get(matchRequirement.style) != null &&
                        result.get(matchRequirement.genderPreference) != null;

        return new CheckUserRes(nicknameExists, allConditionsPresent);
    }

    @Override
    public InfoCheckRes infoCheck(Long userId) {
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        Tuple result = queryFactory
                .select(user.nickname,
                        matchRequirement.team,
                        matchRequirement.teamAllowed,
                        matchRequirement.style,
                        matchRequirement.genderPreference,
                        user.hasAccepted)
                .from(user)
                .leftJoin(matchRequirement).on(matchRequirement.user.id.eq(user.id))
                .where(user.id.eq(userId))
                .fetchOne();

        if (result == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);
        }

        boolean nicknameExists = result.get(user.nickname) != null;

        boolean allConditionsPresent =
                result.get(matchRequirement.team) != null &&
                        result.get(matchRequirement.teamAllowed) != null &&
                        result.get(matchRequirement.style) != null &&
                        result.get(matchRequirement.genderPreference) != null;

        Boolean hasAccepted = result.get(user.hasAccepted);
        boolean accepted = hasAccepted != null && hasAccepted;

        return new InfoCheckRes(nicknameExists, allConditionsPresent, accepted);
    }

    @Override
    public MyPageInformationBaseRes findMyPageInformation(Long userId) {
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;
        QGroupMember groupMember = QGroupMember.groupMember;

        return queryFactory
                .select(Projections.constructor(
                        MyPageInformationBaseRes.class,
                        user,
                        matchRequirement.team,
                        matchRequirement.style,
                        groupMember.id.count()
                ))
                .from(user)
                .leftJoin(matchRequirement)
                .on(matchRequirement.user.id.eq(user.id))
                .leftJoin(groupMember)
                .on(
                        groupMember.user.id.eq(user.id)
                                .and(groupMember.status.eq(GroupMemberStatus.MATCHED.getValue()))
                )
                .where(user.id.eq(userId))
                .groupBy(
                        user.id,
                        matchRequirement.team,
                        matchRequirement.style,
                        user.avgSeason
                )
                .fetchOne();
    }
}
