package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.user.api.dto.request.AcceptedReq;
import at.mateball.domain.user.api.dto.request.EditUserInfoReq;
import at.mateball.domain.user.api.dto.request.UserInfoV2Req;
import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.service.UserV2Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v2/users")
public class UserV2Controller {
    private final UserV2Service userV2Service;

    public UserV2Controller(UserV2Service userV2Service) {
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
    public ResponseEntity<MateballResponse<?>> updateHasAccepted(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AcceptedReq acceptedReq
    ) {
        Long userId = customUserDetails.getUserId();
        userV2Service.updateHasAccepted(userId, acceptedReq.hasAccepted());

        return ResponseEntity.ofNullable(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }

    @PostMapping("/info")
    @Operation(summary = "사용자 정보 설정 api")
    public ResponseEntity<MateballResponse<?>> createUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UserInfoV2Req userInfoReq
    ) {
        Long userId = customUserDetails.getUserId();
        userV2Service.createUserInfo(userId, userInfoReq);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.OK));
    }

    @PutMapping("/info")
    @Operation(summary = "사용자 정보 수정 api")
    public ResponseEntity<MateballResponse<?>> editUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody EditUserInfoReq editUserInfoReq
    ) {
        Long userId = customUserDetails.getUserId();
        userV2Service.editUserInfo(userId, editUserInfoReq);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.OK));
    }
}
