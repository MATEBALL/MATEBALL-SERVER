package at.mateball.domain.group.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GroupMatchFailScheduler {
    private final GroupMatchFailService groupMatchFailService;

    public GroupMatchFailScheduler(GroupMatchFailService groupMatchFailService) {
        this.groupMatchFailService = groupMatchFailService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void run() {
        try {
            groupMatchFailService.failExpiredGroups();
        } catch (Exception exception) {
            log.error("그룹 매칭 실패 처리 중 예외 발생", exception);
        }
    }
}
