package at.mateball.domain.chatting.core.repository;

import at.mateball.domain.chatting.core.Chatting;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChattingRepositoryCustom {
    Optional<Chatting> findFirstByIsUsedFalse();
}
