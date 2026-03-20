package at.mateball.domain.group.core;

import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.chatting.core.service.ChattingV2Service;
import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.group.scheduler.MatchSchedule;
import at.mateball.domain.group.scheduler.ScheduleType;
import at.mateball.domain.group.scheduler.repository.SchedulerRepository;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.GroupMember;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class GroupExecutorV3 {

    private final EntityManager entityManager;
    private final ChattingV2Service chattingV2Service;
    private final SchedulerRepository schedulerRepository;

    public GroupExecutorV3(EntityManager entityManager, ChattingV2Service chattingV2Service, SchedulerRepository schedulerRepository) {
        this.entityManager = entityManager;
        this.chattingV2Service = chattingV2Service;
        this.schedulerRepository = schedulerRepository;
    }

    @Transactional
    public Long createGroup(Long userId, Long gameId, boolean isGroup) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);
        }

        GameInformation game = entityManager.find(GameInformation.class, gameId);
        LocalDateTime gameDateTime = LocalDateTime.of(game.getGameDate(), game.getGameTime());

        Chatting chatting = chattingV2Service.assignChatting();
        if (chatting == null) {
            throw new BusinessException(BusinessErrorCode.CHATTING_NOT_FOUND);
        }

        Group group = Group.create(user, game, isGroup);
        group.assignChatting(chatting);

        entityManager.persist(group);
        entityManager.flush();

        GroupMember leader = GroupMember.leader(user, group, GroupMemberStatus.PENDING_REQUEST.getValue());
        entityManager.persist(leader);

        schedulerRepository.save(new MatchSchedule(group, gameDateTime.minusMinutes(1), ScheduleType.FAIL));
        schedulerRepository.save(new MatchSchedule(group, gameDateTime, ScheduleType.COMPLETE));

        return group.getId();
    }
}
