package at.mateball.domain.group.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class GroupRepositoryImpl implements GroupRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public GroupRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }
}
