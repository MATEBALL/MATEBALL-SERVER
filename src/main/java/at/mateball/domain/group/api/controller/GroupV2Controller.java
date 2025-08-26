package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
<<<<<<< HEAD
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
=======
>>>>>>> e596730 ([feat/#144] 오픈채팅방 주소 조회 api 구현)
import at.mateball.domain.chatting.api.dto.response.ChattingRes;
import at.mateball.domain.group.core.service.GroupV2Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
=======
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> e596730 ([feat/#144] 오픈채팅방 주소 조회 api 구현)

@RestController
@RequestMapping("/v2/users")

public class GroupV2Controller {
    private final GroupV2Service groupV2Service;

    public GroupV2Controller(GroupV2Service groupV2Service) {
        this.groupV2Service = groupV2Service;
    }

<<<<<<< HEAD
    @GetMapping("/match/{matchId}/chatting")
=======
    @GetMapping("/group/{matchId}/chatting")
>>>>>>> e596730 ([feat/#144] 오픈채팅방 주소 조회 api 구현)
    @Operation(summary = "오픈채팅방 주소 조회 api")
    public ResponseEntity<MateballResponse<?>> getChattingUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = userDetails.getUserId();

        ChattingRes data = groupV2Service.getChattingUrl(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, data));
    }
}
