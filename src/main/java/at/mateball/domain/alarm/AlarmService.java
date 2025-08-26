package at.mateball.domain.alarm;

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
    public void createAlarm(Long userId, AlarmType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        if (type == AlarmType.MATCHED) {
            alarmRepository.markAsReadByUserAndTypes(userId, List.of(AlarmType.NEW_REQUEST, AlarmType.APPROVED));
        }

        Alarm alarm = new Alarm(user, type);
        alarmRepository.save(alarm);
    }


    @Transactional
    public void markAsRead(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.ALARM_NOT_FOUND));
        alarm.markAsRead();
    }

    @Transactional(readOnly = true)
    public List<Alarm> getUnreadAlarms(Long userId) {
        return alarmRepository.findByUserIdAndIsReadFalse(userId);
    }
}