package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import com.querydsl.core.Tuple;
import at.mateball.domain.group.api.dto.DirectGetRes;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepositoryCustom {
    DirectCreateRes findDirectCreateResults(Long userId, Long matchId);

    Optional<GroupCreateRes> findGroupCreateRes(Long matchId);
    List<DirectGetRes> findDirectGroupsAfterDate(LocalDate date);
}
