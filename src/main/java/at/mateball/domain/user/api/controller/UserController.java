package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.user.api.dto.request.NicknameRequest;
import at.mateball.domain.user.core.service.UserService;
import at.mateball.exception.code.SuccessCode;
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
    public MateballResponse<Void> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NicknameRequest nicknameRequest
    ) {
        Long userId = userDetails.getUserId();

        userService.updateNickname(userId, nicknameRequest.nickname());

        return MateballResponse.successWithNoData(SuccessCode.CREATED);
    }
}
