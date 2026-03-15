package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.user.api.dto.response.MyPageInformationRes;
import at.mateball.domain.user.core.service.UserV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
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

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.NO_CONTENT, null));
    }

    @GetMapping("/info")
    @Operation(summary = "마이페이지 정보 조회 api")
    public ResponseEntity<MateballResponse<?>> getMyPageInformation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        MyPageInformationRes userInfo = userV3Service.getMyPageInformation(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, userInfo));
    }
}
