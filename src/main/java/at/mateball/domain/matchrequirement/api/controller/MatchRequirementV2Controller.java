package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.matchrequirement.api.dto.request.MatchRequirementReq;
import at.mateball.domain.matchrequirement.api.dto.response.MatchRequirementRes;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementV2Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/users/")
public class MatchRequirementV2Controller {
    private final MatchRequirementV2Service matchRequirementV2Service;

    public MatchRequirementV2Controller(MatchRequirementV2Service matchRequirementV2Service) {
        this.matchRequirementV2Service = matchRequirementV2Service;
    }

    @GetMapping("/match-condition")
    @Operation(summary = "매칭 조건 조회 api")
    public ResponseEntity<MateballResponse<?>> getMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        MatchRequirementRes result = matchRequirementV2Service.getMatchRequirement(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @PatchMapping("/match-condition")
    @Operation(summary = "매칭 조건 수정 api")
    public ResponseEntity<MateballResponse<?>> updateMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchRequirementReq matchRequirementReq
    ) {
        Long userId = userDetails.getUserId();

        matchRequirementV2Service.setMatchRequirement(userId, matchRequirementReq);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.NO_CONTENT));
    }
}
