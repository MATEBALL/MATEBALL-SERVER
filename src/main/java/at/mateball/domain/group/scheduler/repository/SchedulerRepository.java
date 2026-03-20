package at.mateball.domain.group.scheduler.repository;

import at.mateball.domain.group.core.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends JpaRepository<Group, Long>, SchedulerCustom {
}
