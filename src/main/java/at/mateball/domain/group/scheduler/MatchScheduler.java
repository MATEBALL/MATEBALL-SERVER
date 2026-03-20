package at.mateball.domain.group.scheduler;

import at.mateball.domain.group.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchScheduler {

    private final SchedulerRepository repository;
    private final GroupMatchFailService matchFailService;
    private final GroupMatchCompleteService matchCompleteService;

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void run() {
        log.info("매칭 상태 업데이트 스케줄러 호출 완료");

        LocalDateTime now = LocalDateTime.now();
        List<MatchSchedule> targets = repository.findAllDueSchedules(now);

        for (MatchSchedule s : targets) {
            try {
                if (s.getType().isFail()) {
                    matchFailService.validateFailStatus(s.getGroup().getId());
                } else {
                    matchCompleteService.validateCompleteStatus(s.getGroup().getId());
                }

                s.markExecuted();

            } catch (Exception e) {
                log.error("스케줄 처리 실패 groupId={}", s.getGroup().getId(), e);
            }
        }
    }
}
