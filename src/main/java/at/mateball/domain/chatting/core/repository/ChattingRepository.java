package at.mateball.domain.chatting.core.repository;

import at.mateball.domain.chatting.core.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRepository extends JpaRepository<Chatting, Long>, ChattingRepositoryCustom {
}
