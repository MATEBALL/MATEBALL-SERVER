package at.mateball.scheduler;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.scheduler.MatchSchedule;
import at.mateball.domain.group.scheduler.ScheduleType;
import at.mateball.domain.group.scheduler.repository.SchedulerRepository;
import at.mateball.domain.group.scheduler.MatchScheduler;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.user.core.User;
import at.mateball.domain.gameinformation.core.GameInformation;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MatchSchedulerIntegrationTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private MatchScheduler scheduler;

    // ===================== FAIL =====================
    @Test
    @DisplayName("스케줄 실행 시 FAIL 처리")
    void 그룹원이_생성자_혼자일때_FAIL_테스트() {

        System.out.println("\n================ 🔥그룹원이_생성자_혼자일때 FAIL 테스트 시작 ================\n");

        LocalDateTime now = LocalDateTime.now();

        User userA = new User(1L, "male", "url");
        em.persist(userA);

        GameInformation game = new GameInformation(
                "A", "B",
                now.toLocalDate(),
                now.toLocalTime().minusMinutes(10),
                "stadium"
        );
        em.persist(game);

        Group group = Group.create(userA, game, true);
        em.persist(group);

        GroupMember leader = GroupMember.leader(userA, group,GroupMemberStatus.NEW_REQUEST.getValue());
        em.persist(leader);

        LocalDateTime gameTime = LocalDateTime.of(game.getGameDate(), game.getGameTime());

        MatchSchedule schedule = new MatchSchedule(group, gameTime, ScheduleType.FAIL);
        schedulerRepository.save(schedule);

        em.flush();
        em.clear();

        // ===== when =====
        scheduler.run();

        em.flush();
        em.clear();

        // ===== then =====
        System.out.println("\n========= 📊 FAIL 결과 =========");

        printGroup(group.getId());
        printMembers(group.getId());
        printSchedule(schedule.getId());

        MatchSchedule result = em.find(MatchSchedule.class, schedule.getId());
        assertThat(result.isExecuted()).isTrue();

        System.out.println("\n================ 🔥 FAIL 테스트 종료 ================\n");
    }

    @Test
    @DisplayName("스케줄 실행 시 FAIL 처리")
    void 그룹원이_생성자_혼자면서_승인_대기자가_존재할때_FAIL_테스트() {

        System.out.println("\n================ 🔥 그룹원이_생성자_혼자면서_승인_대기자가_존재할때 FAIL 테스트 시작 ================\n");

        LocalDateTime now = LocalDateTime.now();

        User userA = new User(1L, "male", "url");
        User userB = new User(2L, "female", "url");
        em.persist(userA);
        em.persist(userB);

        GameInformation game = new GameInformation(
                "A", "B",
                now.toLocalDate(),
                now.toLocalTime().minusMinutes(10),
                "stadium"
        );
        em.persist(game);

        Group group = Group.create(userA, game, true);
        em.persist(group);

        GroupMember leader = GroupMember.leader(userA, group,GroupMemberStatus.NEW_REQUEST.getValue());
        GroupMember m2 = new GroupMember(userB, group, false, GroupMemberStatus.AWAITING_APPROVAL.getValue(), false);
        em.persist(leader);
        em.persist(m2);

        LocalDateTime gameTime = LocalDateTime.of(game.getGameDate(), game.getGameTime());

        MatchSchedule schedule = new MatchSchedule(group, gameTime, ScheduleType.FAIL);
        schedulerRepository.save(schedule);

        em.flush();
        em.clear();

        // ===== when =====
        scheduler.run();

        em.flush();
        em.clear();

        // ===== then =====
        System.out.println("\n========= 📊 FAIL 결과 =========");

        printGroup(group.getId());
        printMembers(group.getId());
        printSchedule(schedule.getId());

        MatchSchedule result = em.find(MatchSchedule.class, schedule.getId());
        assertThat(result.isExecuted()).isTrue();

        System.out.println("\n================ 🔥 FAIL 테스트 종료 ================\n");
    }

    // ===================== COMPLETE =====================
    @Test
    @DisplayName("스케줄 실행 시 COMPLETE 처리")
    void COMPLETE_테스트() {

        System.out.println("\n================ 🚀 COMPLETE 테스트 시작 ================\n");

        LocalDateTime now = LocalDateTime.now();

        User userA = new User(1L, "male", "url");
        User userB = new User(2L, "female", "url");

        em.persist(userA);
        em.persist(userB);

        GameInformation game = new GameInformation(
                "A", "B",
                now.toLocalDate(),
                now.toLocalTime().minusMinutes(10),
                "stadium"
        );
        em.persist(game);

        Group group = Group.create(userA, game, true);
        em.persist(group);

        GroupMember m1 = new GroupMember(userB, group, true, GroupMemberStatus.PENDING_REQUEST.getValue(), true);
        GroupMember m2 = new GroupMember(userB, group, true, GroupMemberStatus.MATCHED.getValue(), false);
        em.persist(m1);
        em.persist(m2);

        LocalDateTime gameTime = LocalDateTime.of(game.getGameDate(), game.getGameTime());

        MatchSchedule schedule = new MatchSchedule(group, gameTime, ScheduleType.COMPLETE);
        schedulerRepository.save(schedule);

        em.flush();
        em.clear();

        // ===== when =====
        scheduler.run();

        em.flush();
        em.clear();

        // ===== then =====
        System.out.println("\n========= 📊 COMPLETE 결과 =========");

        printGroup(group.getId());
        printMembers(group.getId());
        printSchedule(schedule.getId());

        MatchSchedule result = em.find(MatchSchedule.class, schedule.getId());
        assertThat(result.isExecuted()).isTrue();

        System.out.println("\n================ 🚀 COMPLETE 테스트 종료 ================\n");
    }

    // ===================== 로그 유틸 =====================

    private void printGroup(Long groupId) {
        Group group = em.find(Group.class, groupId);

        System.out.println("🏟️ 그룹 상태 = "
                + GroupStatus.labelOf(group.getStatus())
                + " (" + group.getStatus() + ")");
    }

    private void printMembers(Long groupId) {
        List<GroupMember> members = em.createQuery(
                        "select gm from GroupMember gm where gm.group.id = :groupId",
                        GroupMember.class
                )
                .setParameter("groupId", groupId)
                .getResultList();

        for (GroupMember m : members) {
            System.out.println(
                    "👤 유저ID=" + m.getUser().getId()
                            + " | 역할=" + roleOf(m)
                            + " | 상태=" + GroupMemberStatus.labelOf(m.getStatus())
                            + " (" + m.getStatus() + ")"
            );
        }
    }

    private void printSchedule(Long scheduleId) {
        MatchSchedule s = em.find(MatchSchedule.class, scheduleId);

        System.out.println("📌 스케줄 실행 여부 = "
                + (s.isExecuted() ? "실행됨 ✅" : "미실행 ❌"));
    }

    private String roleOf(GroupMember m) {
        return m.getIsLeader() ? "👑 생성자" : "🙋 참여자";
    }
}
