package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import at.mateball.domain.user.api.dto.request.MatchRequirementReq;
import at.mateball.exception.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/")
public class MatchRequirementController {
    private final MatchRequirementService matchRequirementService;

    public MatchRequirementController(MatchRequirementService matchRequirementService) {
        this.matchRequirementService = matchRequirementService;
    }

    @PostMapping("/match-condition")
    public ResponseEntity<MateballResponse<?>> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchRequirementReq matchRequirementReq
    ) {
        Long userId = userDetails.getUserId();

        matchRequirementService.setMatchRequirement(userId, matchRequirementReq.team(), matchRequirementReq.teamAllowed(), matchRequirementReq.style(), matchRequirementReq.genderPreference());

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }
}
