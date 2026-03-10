package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.group.api.dto.GroupMemberMatchingRateRes;
import at.mateball.domain.group.core.service.GroupMatchingQueryService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users")
public class GroupV3Controller {

    private final GroupMatchingQueryService groupMatchingQueryService;

    @GetMapping("/groups/{groupId}/match-rate")
    @Operation(summary = "로그인 사용자 기준 그룹원별 매칭률 조회 API")
    public ResponseEntity<MateballResponse<List<GroupMemberMatchingRateRes>>> getGroupMemberMatchingRates(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long groupId
    ) {
        Long userId = customUserDetails.getUserId();

        List<GroupMemberMatchingRateRes> response =
                groupMatchingQueryService.getGroupMemberMatchingRates(userId, groupId);

        return ResponseEntity.ok(
                MateballResponse.success(SuccessCode.OK, response)
        );
    }
}