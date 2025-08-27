package at.mateball.domain.chatting.core.repository;

import at.mateball.domain.chatting.core.Chatting;

import java.util.Optional;

public interface ChattingRepositoryCustom {
    Optional<Chatting> findFirstByIsUsedFalseOrderByIdAsc();
}
