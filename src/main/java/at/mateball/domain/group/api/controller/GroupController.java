package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.group.api.dto.DirectCreateRes;
import at.mateball.domain.group.api.dto.DirectGetListRes;
import at.mateball.domain.group.api.dto.GroupCreateRes;
import at.mateball.domain.group.api.dto.GroupGetListRes;
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

    @GetMapping("/group/{matchId}")
    @CustomExceptionDescription(SwaggerResponseDescription.GROUP_MATCHING)
    @Operation(summary = "그룹 매칭 생성 결과 화면입니다.")
    public ResponseEntity<MateballResponse<?>> getGroupMatching(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();
        GroupCreateRes groupCreateRes = groupService.getGroupMatching(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, groupCreateRes));
    }

    @CustomExceptionDescription(SwaggerResponseDescription.NO_BUSINESS_ERROR)
    @Operation(summary = "매칭 요청")
    @PostMapping("/match-request/{matchId}")
    public ResponseEntity<MateballResponse<?>> requestMatching(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        groupService.requestMatching(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }

    @CustomExceptionDescription(SwaggerResponseDescription.NO_BUSINESS_ERROR)
    @Operation(summary = "그룹 매칭 리스트 조회")
    @GetMapping("/group")
    public ResponseEntity<MateballResponse<?>> getGroups(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @RequestParam LocalDate date
    ) {
        Long userId = customUserDetails.getUserId();
        GroupGetListRes groupGetListRes = groupService.getGroups(userId, date);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, groupGetListRes));
    }

    @CustomExceptionDescription(SwaggerResponseDescription.PATCH_MATCH_ACCEPT)
    @Operation(summary = "요청 수락")
    @PatchMapping("/match-accept/{matchId}")
    public ResponseEntity<MateballResponse<?>> permitRequest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        groupService.permitRequest(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.NO_CONTENT));
    }

    @PatchMapping("/match-reject/{matchId}")
    public ResponseEntity<MateballResponse<?>> rejectRequest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        groupService.rejectRequest(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.NO_CONTENT));
    }
}
