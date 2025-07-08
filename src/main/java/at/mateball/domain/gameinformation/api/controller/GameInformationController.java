package at.mateball.domain.gameinformation.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.gameinformation.api.dto.response.GameInformationsRes;
import at.mateball.domain.gameinformation.core.service.GameInformationService;
import at.mateball.exception.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/users/")
public class GameInformationController {
    private final GameInformationService gameInformationService;

    public GameInformationController(GameInformationService gameInformationService) {
        this.gameInformationService = gameInformationService;
    }

    @GetMapping("/game/schedule")
    public ResponseEntity<MateballResponse<GameInformationsRes>> getUserInformation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("date") LocalDate gameInformationReq
    ) {
        Long userId = userDetails.getUserId();

        GameInformationsRes data = gameInformationService.getGameInformation(userId, gameInformationReq);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, data));
    }
}
