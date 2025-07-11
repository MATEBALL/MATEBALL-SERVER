package at.mateball.domain.group.scheduler;

import at.mateball.domain.group.core.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class GroupMatchFailService {
    private final GroupRepository groupRepository;

    public GroupMatchFailService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional
    public void failExpiredGroups() {
        LocalDate date = calculateTargetDate(LocalDate.now());
        log.info("[스케줄러 테스트] 기준 날짜 = {}", date);

        List<Long> matchId = groupRepository.findGroupIdsByGameDate(date);
        log.info("[스케줄러 테스트] 해당 날짜 그룹 수 = {}", matchId.size());

        if (matchId.isEmpty()) {
            log.info("처리 대상 그룹 없음 - 기준일 : {}", date);
            return;
        }

        int groupCnt = groupRepository.bulkUpdateGroupStatusToFailed(matchId);
        int memberCnt = groupRepository.bulkUpdateGroupMemberStatusToMatchFailed(matchId);
        log.info("해당 날짜: {}, 그룹 실패 처리 {}건, 멤버 실패 처리 {}건", matchId, groupCnt, memberCnt);
    }

    private LocalDate calculateTargetDate(LocalDate today) {
        DayOfWeek day = today.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY) {
            return today.plusDays(3);
        }

        return today.plusDays(2);
    }
}
