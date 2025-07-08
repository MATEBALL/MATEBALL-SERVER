package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.DirectBaseRes;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GroupRepositoryCustom {
    DirectCreateRes findDirectCreateResults(Long userId, Long matchId);

    List<DirectBaseRes> findDirectGroupsByDate(LocalDate date);
}
