package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.service.UserV3Service;
import at.mateball.exception.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users")
public class UserV3Controller {
    private final UserV3Service userV3Service;

    @PatchMapping("/onboarding")
    public ResponseEntity<MateballResponse<?>> deleteOnboarding(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        userV3Service.clearOnboardingInfo(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.NO_CONTENT, null));    }
}
