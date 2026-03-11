package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.matchrequirement.api.dto.request.MatchRequirementV3Req;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementV3Service;
import at.mateball.domain.user.core.service.UserV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users/match-condition")
public class MatchRequirementV3Controller {
    private final MatchRequirementV3Service matchRequirementV3Service;

    @PostMapping
    @Operation(summary = "매칭 조건 설정 api")
    public ResponseEntity<MateballResponse<?>> setMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchRequirementV3Req matchRequirementV3Req
    ) {
        Long userId = userDetails.getUserId();

        matchRequirementV3Service.setAvgSeasonAndMatchRequirement(
                userId,
                matchRequirementV3Req.team(),
                matchRequirementV3Req.teamAllowed(),
                matchRequirementV3Req.style(),
                matchRequirementV3Req.avgSeason()
        );

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }
}
