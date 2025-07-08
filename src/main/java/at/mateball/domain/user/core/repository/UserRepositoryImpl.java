package at.mateball.domain.user.core.repository;

import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.api.dto.response.UserInformationRes;
import at.mateball.domain.user.core.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public UserInformationRes findUserInformation(Long userId) {
        QUser user = QUser.user;
        QMatchRequirement matchRequirement = QMatchRequirement.matchRequirement;

        Tuple tuple = queryFactory
                .select(
                        user.nickname,
                        user.birthYear,
                        user.gender,
                        matchRequirement.style,
                        user.introduction,
                        user.imgUrl
                )
                .from(user)
                .join(matchRequirement).on(user.id.eq(matchRequirement.user.id))
                .where(user.id.eq(userId))
                .fetchOne();

        return tuple != null ? UserInformationRes.from(tuple) : null;
    }
}
