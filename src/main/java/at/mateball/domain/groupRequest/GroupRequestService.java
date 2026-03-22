package at.mateball.domain.groupRequest;

import at.mateball.domain.alarm.common.AlarmType;
import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.group.api.dto.GroupValidationRes;
import at.mateball.domain.group.api.dto.RequestValidationRes;
import at.mateball.domain.group.core.repository.GroupRepository;
import at.mateball.domain.group.infrastructure.repository.GroupV3RepositoryCustom;
import at.mateball.domain.groupmember.GroupMemberStatus;
import at.mateball.domain.groupmember.core.repository.GroupMemberRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupRequestService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupV3RepositoryCustom groupV3RepositoryCustom;
    private final AlarmService alarmService;
    private final GroupRequestValidator groupRequestValidator;

    @Transactional
    public void createRequest(Long userId, Long groupId) {
        GroupValidationRes group = groupRepository.findValidateGroupData(groupId);
        if (group == null) {
            throw new BusinessException(BusinessErrorCode.GROUP_NOT_FOUND);
        }

        RequestValidationRes validationData = groupV3RepositoryCustom.getValidation(userId, groupId);
        groupRequestValidator.validateCreateRequest(userId, group, validationData);

        try {
            groupMemberRepository.createGroupMemberV3(userId, groupId);
            groupMemberRepository.updateMemberStatus(group.leaderId(), groupId, GroupMemberStatus.NEW_REQUEST.getValue());
            alarmService.createAlarm(group.leaderId(), AlarmType.NEW_REQUEST, groupId);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_MATCH_REQUEST);
        }
    }
}
