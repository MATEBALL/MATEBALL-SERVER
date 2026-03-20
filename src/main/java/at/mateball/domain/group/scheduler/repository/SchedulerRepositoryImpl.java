package at.mateball.domain.group.scheduler.repository;

import at.mateball.domain.group.scheduler.MatchSchedule;
import at.mateball.domain.group.scheduler.QMatchSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class SchedulerRepositoryImpl implements SchedulerCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public SchedulerRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public void save(MatchSchedule schedule) {
        em.persist(schedule);
    }

    @Override
    public List<MatchSchedule> findAllDueSchedules(LocalDateTime now) {
        QMatchSchedule matchSchedule = QMatchSchedule.matchSchedule;

        return queryFactory
                .selectFrom(matchSchedule)
                .where(
                        matchSchedule.executed.isFalse(),
                        matchSchedule.executeTime.loe(now)
                )
                .fetch();
    }
}
