package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.service.UserV2Service;
import at.mateball.exception.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2/users")
public class UserV2Contrller {
    private final UserV2Service userV2Service;

    public UserV2Contrller(UserV2Service userV2Service) {
        this.userV2Service = userV2Service;
    }

    @GetMapping("/info-check")
    public ResponseEntity<MateballResponse<?>> getInfoCheck(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        InfoCheckRes infoCheckRes = userV2Service.getInfoCheck(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, infoCheckRes));
    }

    @PostMapping("/consent")
    public ResponseEntity<MateballResponse<?>> updateHasAccepted
}
