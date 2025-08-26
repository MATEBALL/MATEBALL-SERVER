package at.mateball.domain.user.core.service;

import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserV2Service {
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    public UserV2Service(UserRepository userRepository, AlarmService alarmService) {
        this.userRepository = userRepository;
        this.alarmService = alarmService;
    }

    public InfoCheckRes getInfoCheck(Long userId) {
        InfoCheckRes infoCheckRes = userRepository.infoCheck(userId);

        boolean isAlarm = alarmService.hasUnreadAlarm(userId);

        return new InfoCheckRes(
                infoCheckRes.nickname(),
                infoCheckRes.condition(),
                infoCheckRes.hasAccepted(),
                isAlarm
        );
    }
}
