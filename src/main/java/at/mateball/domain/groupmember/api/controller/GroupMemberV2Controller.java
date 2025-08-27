package at.mateball.domain.groupmember.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.domain.groupmember.api.dto.DirectStatusListRes;
import at.mateball.domain.groupmember.api.dto.GroupStatusListRes;
import at.mateball.domain.groupmember.core.service.GroupMemberV2Service;
import at.mateball.exception.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/users")
public class GroupMemberV2Controller {
    private final GroupMemberV2Service groupMemberV2Service;

    public GroupMemberV2Controller(GroupMemberV2Service groupMemberV2Service) {
        this.groupMemberV2Service = groupMemberV2Service;
    }

    @GetMapping("/match-stage/direct")
    public ResponseEntity<MateballResponse<?>> getDirectStatusV2(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "status", required = false) String statusLabel
    ) {
        Long userId = customUserDetails.getUserId();
        DirectStatusListRes result;

        if (statusLabel == null || statusLabel.isBlank()) {
            result = groupMemberV2Service.getAllDirectStatusV2(userId);
        } else {
            GroupStatus groupStatus = GroupStatus.fromCode(statusLabel);
            result = groupMemberV2Service.getDirectStatusV2(userId, groupStatus);
        }

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @GetMapping("/match-stage/group")
    public ResponseEntity<MateballResponse<?>> getGroupStatusV2(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "status", required = false) String statusLabel
    ) {
        Long userId = customUserDetails.getUserId();
        GroupStatusListRes result;

        if (statusLabel == null || statusLabel.isBlank()) {
            result = groupMemberV2Service.getAllGroupStatusV2(userId);
        } else {
            GroupStatus groupStatus = GroupStatus.fromCode(statusLabel);
            result = groupMemberV2Service.getGroupStatusV2(userId, groupStatus);
        }

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }
}
