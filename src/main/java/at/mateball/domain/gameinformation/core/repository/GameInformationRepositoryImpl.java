package at.mateball.domain.gameinformation.core.repository;

import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.gameinformation.core.QGameInformation;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDate;
import java.util.List;

public class GameInformationRepositoryImpl implements GameInformationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GameInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<GameInformation> findByGameDate(LocalDate gameDate) {
        QGameInformation gameInformation = QGameInformation.gameInformation;

        return queryFactory
                .selectFrom(gameInformation)
                .where(gameInformation.gameDate.eq(gameDate))
                .fetch();
    }

    ;

}
