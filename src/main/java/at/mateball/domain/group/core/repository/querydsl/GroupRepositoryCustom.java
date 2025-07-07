package at.mateball.domain.group.core.repository.querydsl;

import at.mateball.domain.group.api.dto.DirectCreateRes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepositoryCustom {
    List<DirectCreateRes> findDirectCreateResults(Long userId);
}
