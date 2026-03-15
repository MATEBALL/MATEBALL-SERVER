package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.matchrequirement.api.dto.request.MatchRequirementV3Req;
import at.mateball.domain.matchrequirement.core.repository.querydsl.MatchRequirementUpdateReq;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementAndAvgSeasonService;
import at.mateball.domain.matchrequirement.api.dto.response.MatchRequirementAndAvgSeasonRes;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users/match-condition")
public class MatchRequirementV3Controller {
    private final MatchRequirementAndAvgSeasonService matchRequirementAndAvgSeasonService;
    private final MatchRequirementV3Service matchRequirementV3Service;

    @PostMapping
    @Operation(summary = "매칭 조건 설정 api")
    public ResponseEntity<MateballResponse<?>> setOnboardingInformation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchRequirementV3Req onboardingReq
    ) {
        Long userId = userDetails.getUserId();

        matchRequirementAndAvgSeasonService.setMatchRequirementAndAvgSeason(
                userId,
                onboardingReq.team(),
                onboardingReq.teamAllowed(),
                onboardingReq.style(),
                onboardingReq.avgSeason()
        );

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }

    @GetMapping
    @Operation(summary = "매칭 조건 조회 api")
    public ResponseEntity<MateballResponse<?>> getMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        MatchRequirementAndAvgSeasonRes result = matchRequirementAndAvgSeasonService.getMatchRequirementAndAvgSeason(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @DeleteMapping
    public ResponseEntity<MateballResponse<?>> deleteMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        matchRequirementV3Service.deleteByUserId(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.NO_CONTENT, null));
    }

    @PatchMapping
    @Operation(summary = "매칭 조건 수정 api")
    public ResponseEntity<MateballResponse<?>> updateMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchRequirementUpdateReq matchRequirementUpdateReq
    ) {
        Long userId = userDetails.getUserId();

        matchRequirementAndAvgSeasonService.updateMatchRequirementAndAvgSeason(userId, matchRequirementUpdateReq);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.NO_CONTENT));
      
    @DeleteMapping
    public ResponseEntity<MateballResponse<?>> deleteMatchRequirement(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        matchRequirementV3Service.deleteByUserId(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.NO_CONTENT, null));
    }
}
