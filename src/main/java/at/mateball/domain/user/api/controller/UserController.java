package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.user.api.dto.request.NicknameReq;
import at.mateball.domain.user.core.service.UserService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/info/nickname")
    @CustomExceptionDescription(SwaggerResponseDescription.UPDATE_NICKNAME)
    @Operation(summary = "닉네임 설정 api")
    public ResponseEntity<MateballResponse<?>> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NicknameReq nicknameReq
    ) {
        Long userId = userDetails.getUserId();

        userService.updateNickname(userId, nicknameReq.nickname());

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }
}
