package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.group.api.dto.GroupMatchRes;
import at.mateball.domain.group.core.service.GroupV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users")
public class GroupV3Controller {

    private final GroupV3Service groupV3Service;

    @GetMapping("/match/{gameId}")
    @Operation(summary = "특정 경기 기준 매칭 가능한 그룹 및 개인 목록 조회 API")
    public ResponseEntity<MateballResponse<GroupMatchRes>> getAvailableMatches(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long gameId
    ) {
        Long userId = customUserDetails.getUserId();

        GroupMatchRes result = groupV3Service.getGroupMatchs(userId, gameId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));

    }
}