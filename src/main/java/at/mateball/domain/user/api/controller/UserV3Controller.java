package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.user.api.dto.response.MyPageInformationRes;
import at.mateball.domain.chatting.api.dto.response.ChattingRes;
import at.mateball.domain.group.core.service.GroupV3Service;
import at.mateball.domain.user.core.service.UserV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users")
public class UserV3Controller {
    private final UserV3Service userV3Service;
    private final GroupV3Service groupV3Service;

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

    @GetMapping("/match/{matchId}/chatting")
    @Operation(summary = "오픈채팅방 주소 조회 api")
    public ResponseEntity<MateballResponse<?>> getChattingUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = userDetails.getUserId();

        ChattingRes data = groupV3Service.getChattingUrl(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, data));
    }

    @GetMapping("/count")
    @Operation(summary = "가입자 수 조회 api")
    public ResponseEntity<MateballResponse<?>> getUsersCount(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        UserCountRes response = userService.getUsersCount(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, response));
    }
}
