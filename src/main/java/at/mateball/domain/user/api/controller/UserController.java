package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.user.api.dto.request.NicknameReq;
import at.mateball.domain.user.api.dto.response.UserInformationRes;
import at.mateball.domain.user.api.dto.response.KaKaoInformationRes;
import at.mateball.domain.user.core.service.UserService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/kakao/info")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_KAKAO_INFORMATION)
    @Operation(summary = "카카오에서 받아온 사용자 정보 api")
    public ResponseEntity<MateballResponse<?>> getKaKaoInformation(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        KaKaoInformationRes data = userService.getKakaoInformation(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, data));

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

    @GetMapping("/info")
    @CustomExceptionDescription(SwaggerResponseDescription.UPDATE_NICKNAME)
    @Operation(summary = "내 정보 조회 api")
    public MateballResponse<UserInformationRes> getUserInformation(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        UserInformationRes data = userService.getUserInformation(userId);

        return MateballResponse.success(SuccessCode.OK, data);

    }
}
