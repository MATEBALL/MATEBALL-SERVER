package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.chatting.api.dto.response.ChattingRes;
import at.mateball.domain.group.core.service.GroupV2Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/users")

public class GroupV2Controller {
    private final GroupV2Service groupV2Service;

    public GroupV2Controller(GroupV2Service groupV2Service) {
        this.groupV2Service = groupV2Service;
    }

    @GetMapping("/match/{matchId}/chatting")
    @Operation(summary = "오픈채팅방 주소 조회 api")
    public ResponseEntity<MateballResponse<?>> getChattingUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = userDetails.getUserId();

        ChattingRes data = groupV2Service.getChattingUrl(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, data));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "클라이언트용 사용자 매칭 모두 제거하는 api")
    public ResponseEntity<MateballResponse<?>> deleteMatch(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();

        groupV2Service.delelteMatch(userId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.OK));
    }
}
