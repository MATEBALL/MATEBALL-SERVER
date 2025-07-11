package at.mateball.domain.group.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMatchFailScheduler {
    private final GroupMatchFailScheduler groupMatchFailScheduler;

    @Scheduled(cron = "0 0 3 * * *")
    public void run() {
        try {
            groupMatchFailScheduler.failExpiredGroups();
        } catch (Exception exception) {
            log.error("그룹 매칭 실패 처리 중 예외 발생", exception);
        }
    }
}
