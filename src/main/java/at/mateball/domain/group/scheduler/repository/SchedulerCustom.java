package at.mateball.domain.group.scheduler.repository;

import at.mateball.domain.group.scheduler.MatchSchedule;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerCustom {
    void save(MatchSchedule schedule);

    List<MatchSchedule> findAllDueSchedules(LocalDateTime now);
}
