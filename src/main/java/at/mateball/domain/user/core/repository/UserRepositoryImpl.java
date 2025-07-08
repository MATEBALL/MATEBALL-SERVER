package at.mateball.domain.user.core.repository;

import at.mateball.domain.matchrequirement.core.QMatchRequirement;
import at.mateball.domain.user.api.dto.response.UserInformationRes;
import at.mateball.domain.user.core.QUser;
import at.mateball.domain.user.core.User;
import com.querydsl.core.Tuple;
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
