package at.mateball.domain.chatting.core.repository;

import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.chatting.core.QChatting;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

public class ChattingRepositoryImpl implements ChattingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChattingRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Chatting> findFirstByIsUsedFalse() {
        QChatting chatting = QChatting.chatting;

        Chatting result = queryFactory
                .selectFrom(chatting)
                .where(chatting.isUsed.eq(false))
                .orderBy(chatting.id.asc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}
