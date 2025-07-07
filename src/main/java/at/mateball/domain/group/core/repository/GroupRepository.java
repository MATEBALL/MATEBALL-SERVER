package at.mateball.domain.group.core.repository;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.repository.querydsl.GroupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {
}
