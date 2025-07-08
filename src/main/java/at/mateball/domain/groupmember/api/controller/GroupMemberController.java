package at.mateball.domain.groupmember.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.DirectStatusListRes;
import at.mateball.domain.groupmember.api.dto.GroupStatusListRes;
import at.mateball.domain.groupmember.core.service.GroupMemberService;
import at.mateball.exception.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

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
}
