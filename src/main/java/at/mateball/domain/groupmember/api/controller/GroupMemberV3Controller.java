package at.mateball.domain.groupmember.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.groupmember.api.dto.MatchRequestDetailRes;
import at.mateball.domain.groupmember.core.service.GroupMemberV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/users")
@RequiredArgsConstructor
public class GroupMemberV3Controller {

    private final GroupMemberV3Service groupMemberV3Service;

    @Operation(summary = "매칭 요청 상세 조회")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<MateballResponse<List<MatchRequestDetailRes>>> getMatchRequestDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = userDetails.getUserId();

        List<MatchRequestDetailRes> response = groupMemberV3Service.getMatchRequestDetails(userId, matchId);

        return ResponseEntity.ok(
                MateballResponse.success(SuccessCode.OK, response)
        );
    }
}
