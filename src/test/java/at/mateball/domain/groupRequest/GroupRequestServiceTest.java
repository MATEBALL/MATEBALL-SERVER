package at.mateball.domain.groupRequest;

import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.chatting.core.service.ChattingV2Service;
import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.groupmember.api.dto.GroupMatchSummaryRes;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.domain.groupmember.core.service.GroupMemberV3Service;
import at.mateball.domain.user.core.User;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GroupRequestServiceTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;
    @Mock
    private at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom groupV3RepositoryCustom;
    @Mock
    private AlarmService alarmService;
    @Mock
    private GroupMemberV3Service groupMemberV3Service;
    @Mock
    private GroupRequestValidator groupRequestValidator;
    @Mock
    private at.mateball.exception.ConstraintExceptionTranslator constraintExceptionTranslator;
    @Mock
    private ChattingV2Service chattingV2Service;
    @Mock
    private jakarta.persistence.EntityManager entityManager;

    @InjectMocks
    private GroupRequestService groupRequestService;

    private static final Long LEADER_ID = 1L;
    private static final Long REQUESTER_ID = 2L;
    private static final Long GROUP_ID = 100L;
    private static final Long CHATTING_ID = 55L;

    private Group mockPendingGroup(boolean isGroup, Chatting assignedChatting) {
        Group group = org.mockito.Mockito.mock(Group.class);
        User leader = org.mockito.Mockito.mock(User.class);
        when(group.getStatus()).thenReturn(GroupStatus.PENDING.getValue());
        when(group.getLeader()).thenReturn(leader);
        when(leader.getId()).thenReturn(LEADER_ID);
        when(group.isGroup()).thenReturn(isGroup);
        when(group.getId()).thenReturn(GROUP_ID);
        when(group.getChatting()).thenReturn(assignedChatting);
        return group;
    }

    @Test
    @DisplayName("케이스 A: 다이렉트 매칭 수락 시 채팅방이 없으면 새로 배정한다")
    void permitRequest_direct_assignsChattingWhenAbsent() {
        Group group = mockPendingGroup(false, null);
        when(groupRepository.findGroupWithLock(GROUP_ID)).thenReturn(Optional.of(group));
        when(groupMemberRepository.getMatchSummary(GROUP_ID))
                .thenReturn(Optional.of(new GroupMatchSummaryRes(REQUESTER_ID, 0L)));

        Chatting chatting = org.mockito.Mockito.mock(Chatting.class);
        when(chatting.getId()).thenReturn(CHATTING_ID);
        when(chattingV2Service.assignChatting()).thenReturn(chatting);

        groupRequestService.permitRequest(LEADER_ID, GROUP_ID);

        verify(chattingV2Service).assignChatting();
        verify(groupRepository).assignChattingToGroup(GROUP_ID, CHATTING_ID);
    }

    @Test
    @DisplayName("케이스 B: 이미 채팅방이 배정된 매칭은 새 채팅방을 배정하지 않는다")
    void permitRequest_skipsAssignWhenAlreadyAssigned() {
        Chatting existing = org.mockito.Mockito.mock(Chatting.class);
        Group group = mockPendingGroup(false, existing);
        when(groupRepository.findGroupWithLock(GROUP_ID)).thenReturn(Optional.of(group));
        when(groupMemberRepository.getMatchSummary(GROUP_ID))
                .thenReturn(Optional.of(new GroupMatchSummaryRes(REQUESTER_ID, 0L)));

        groupRequestService.permitRequest(LEADER_ID, GROUP_ID);

        verify(chattingV2Service, never()).assignChatting();
        verify(groupRepository, never()).assignChattingToGroup(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("케이스 C/D: 가용 채팅방이 없으면 CHATTING_NOT_FOUND 가 전파되고 그룹에 링크하지 않는다")
    void permitRequest_propagatesWhenNoChattingAvailable() {
        Group group = mockPendingGroup(false, null);
        when(groupRepository.findGroupWithLock(GROUP_ID)).thenReturn(Optional.of(group));
        when(groupMemberRepository.getMatchSummary(GROUP_ID))
                .thenReturn(Optional.of(new GroupMatchSummaryRes(REQUESTER_ID, 0L)));
        when(chattingV2Service.assignChatting())
                .thenThrow(new BusinessException(BusinessErrorCode.CHATTING_NOT_FOUND));

        assertThatThrownBy(() -> groupRequestService.permitRequest(LEADER_ID, GROUP_ID))
                .isInstanceOf(BusinessException.class);

        verify(groupRepository, never()).assignChattingToGroup(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("그룹 매칭은 정원이 차지 않으면 채팅방을 배정하지 않는다")
    void permitRequest_group_notFull_doesNotAssign() {
        Group group = mockPendingGroup(true, null);
        when(groupRepository.findGroupWithLock(GROUP_ID)).thenReturn(Optional.of(group));
        // matchedParticipants + 1 = 2 != 4 -> 정원 미달
        when(groupMemberRepository.getMatchSummary(GROUP_ID))
                .thenReturn(Optional.of(new GroupMatchSummaryRes(REQUESTER_ID, 1L)));

        groupRequestService.permitRequest(LEADER_ID, GROUP_ID);

        verify(chattingV2Service, never()).assignChatting();
        verify(groupRepository, never()).assignChattingToGroup(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("그룹 매칭은 정원이 차면(4명) 채팅방을 배정한다")
    void permitRequest_group_full_assignsChatting() {
        Group group = mockPendingGroup(true, null);
        when(groupRepository.findGroupWithLock(GROUP_ID)).thenReturn(Optional.of(group));
        // matchedParticipants + 1 = 4 -> 정원 충족
        when(groupMemberRepository.getMatchSummary(GROUP_ID))
                .thenReturn(Optional.of(new GroupMatchSummaryRes(REQUESTER_ID, 3L)));

        Chatting chatting = org.mockito.Mockito.mock(Chatting.class);
        when(chatting.getId()).thenReturn(CHATTING_ID);
        when(chattingV2Service.assignChatting()).thenReturn(chatting);

        groupRequestService.permitRequest(LEADER_ID, GROUP_ID);

        verify(chattingV2Service).assignChatting();
        verify(groupRepository).assignChattingToGroup(GROUP_ID, CHATTING_ID);
    }
}
