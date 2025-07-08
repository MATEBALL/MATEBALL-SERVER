package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.core.service.GroupService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import at.mateball.common.swagger.SwaggerResponseDescription;

@RestController
@RequestMapping("/v1/users")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/direct/{matchId}")
    public ResponseEntity<MateballResponse<?>> getDirectMatching(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();
        DirectCreateRes directCreateRes = groupService.getDirectMatching(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, directCreateRes));
    }

    @GetMapping("/group/{matchId}")
    @CustomExceptionDescription(SwaggerResponseDescription.GROUP_MATCHING)
    @Operation(summary = "어떤 역할의 api인지 작성합니다", description = "api에 대한 세부 설명을 작성합니다")
    public ResponseEntity<MateballResponse<?>> getGroupMatching(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();
        GroupCreateRes groupCreateRes = groupService.getGroupMatching(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, groupCreateRes));
    }
}
