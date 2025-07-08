package at.mateball.domain.user.core.repository;

import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.api.dto.response.BaseUserInformationRes;
import at.mateball.domain.user.api.dto.response.UserInformationRes;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.user.core.User;
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
                        BaseUserInformationRes.class,
                        user.nickname,
                        user.birthYear,
                        user.gender,
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
}
