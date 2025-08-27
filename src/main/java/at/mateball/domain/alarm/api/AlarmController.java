package at.mateball.domain.alarm.api;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.alarm.api.dto.AlarmRes;
import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.exception.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/users")
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping("/alarm")
    public ResponseEntity<MateballResponse<?>> getAlarm(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();

        boolean hasUnreadAlarm = alarmService.hasUnreadAlarm(userId);
        AlarmRes alarmRes = new AlarmRes(hasUnreadAlarm);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, alarmRes));
    }

    @PostMapping("/alarm/{matchId}")
    public ResponseEntity<MateballResponse<?>> updateAlarm(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        alarmService.updateAlarm(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.OK));
    }
}
