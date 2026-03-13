package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.matchrequirement.api.dto.request.MatchRequirementV3Req;
import at.mateball.domain.matchrequirement.core.service.OnboardingService;
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
    private final OnboardingService onboardingService;

    @PostMapping
    @Operation(summary = "매칭 조건 설정 api")
    public ResponseEntity<MateballResponse<?>> setOnboardingInformation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchRequirementV3Req onboardingReq
    ) {
        Long userId = userDetails.getUserId();

        onboardingService.setOnboardingInformation(
                userId,
                onboardingReq.team(),
                onboardingReq.teamAllowed(),
                onboardingReq.style(),
                onboardingReq.avgSeason()
        );

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }
}
