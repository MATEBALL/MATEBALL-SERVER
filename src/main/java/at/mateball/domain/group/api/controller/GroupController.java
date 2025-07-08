package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.DirectGetListRes;
import at.mateball.domain.group.core.service.GroupService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/users")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/direct/{matchId}")
    @CustomExceptionDescription(SwaggerResponseDescription.DIRECT_MATCHING)
    @Operation(summary = "일대일 매칭 생성 결과 조회")
    public ResponseEntity<MateballResponse<?>> getDirectMatching(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();
        DirectCreateRes directCreateRes = groupService.getDirectMatching(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, directCreateRes));
    }

    @GetMapping("/direct")
    @CustomExceptionDescription(SwaggerResponseDescription.DIRECT_MATCH)
    @Operation(summary = "일대일 매칭 리스트 반환")
    public ResponseEntity<MateballResponse<?>> getDirects(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @RequestParam LocalDate date
    ) {
        Long userId = customUserDetails.getUserId();
        DirectGetListRes directGetListRes = groupService.getDirects(userId, date);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, directGetListRes));
    }
}
