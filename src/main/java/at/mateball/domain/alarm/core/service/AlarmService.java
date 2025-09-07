package at.mateball.domain.alarm.core.service;

import at.mateball.domain.alarm.common.AlarmType;
import at.mateball.domain.alarm.core.Alarm;
import at.mateball.domain.alarm.core.repository.AlarmRepository;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createAlarm(Long userId, AlarmType type, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        if (type == AlarmType.MATCHED) {
            alarmRepository.markAsReadByUserAndTypes(userId, List.of(AlarmType.NEW_REQUEST, AlarmType.APPROVED));
        }

        Alarm alarm = new Alarm(user, type, groupId);
        alarmRepository.save(alarm);
    }

    @Transactional
    public void updateAlarm(Long userId, Long matchId) {
        List<Alarm> alarms = alarmRepository.findAllByUserIdAndGroupId(userId, matchId);
        if (alarms.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.ALARM_NOT_FOUND);
        }
        alarms.forEach(Alarm::markAsRead);
    }
}