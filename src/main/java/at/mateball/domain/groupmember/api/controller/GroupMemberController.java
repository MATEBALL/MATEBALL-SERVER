package at.mateball.domain.groupmember.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.DetailMatchingListRes;
import at.mateball.domain.groupmember.api.dto.DirectStatusListRes;
import at.mateball.domain.groupmember.api.dto.GroupMemberCountRes;
import at.mateball.domain.groupmember.api.dto.GroupStatusListRes;
import at.mateball.domain.groupmember.core.service.GroupMemberService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import at.mateball.common.swagger.SwaggerResponseDescription;

@RestController
@RequestMapping("/v1/users")
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @CustomExceptionDescription(SwaggerResponseDescription.NO_BUSINESS_ERROR)
    @Operation(summary = "일대일 매칭 현황 조회")
    @GetMapping("/match-stage/direct")
    public ResponseEntity<MateballResponse<?>> getDirectStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "status", required = false) String statusLabel
    ) {
        Long userId = userDetails.getUserId();
        DirectStatusListRes result;

        if (statusLabel == null || statusLabel.isBlank()) {
            result = groupMemberService.getAllDirectStatus(userId);
        } else {
            GroupStatus groupStatus = GroupStatus.fromCode(statusLabel);
            result = groupMemberService.getDirectStatus(userId, groupStatus);
        }

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @CustomExceptionDescription(SwaggerResponseDescription.NO_BUSINESS_ERROR)
    @Operation(summary = "그룹 매칭 현황 조회")
    @GetMapping("/match-stage/group")
    public ResponseEntity<MateballResponse<?>> getGroupStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "status", required = false) String statusLabel
    ) {
        Long userId = userDetails.getUserId();
        GroupStatusListRes result;

        if (statusLabel == null || statusLabel.isBlank()) {
            result = groupMemberService.getAllGroupStatus(userId);
        } else {
            GroupStatus groupStatus = GroupStatus.fromCode(statusLabel);
            result = groupMemberService.getGroupStatus(userId, groupStatus);
        }

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @GetMapping("/num-count/{matchId}")
    public ResponseEntity<MateballResponse<?>> countGroupMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        GroupMemberCountRes groupMemberCountRes = groupMemberService.countGroupMember(matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, groupMemberCountRes));
    }

    @GetMapping("/match-detail/{matchId}")
    public ResponseEntity<MateballResponse<?>> getDetailMatching(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = userDetails.getUserId();
        DetailMatchingListRes detailMatchingListRes = groupMemberService.getDetailMatching(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, detailMatchingListRes));
    }

    @PatchMapping("/match-stage/{matchId}")
    public ResponseEntity<MateballResponse<?>> updateStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = userDetails.getUserId();
        groupMemberService.updateStatus(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.NO_CONTENT));
    }
}
