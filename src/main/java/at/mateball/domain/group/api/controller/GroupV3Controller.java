package at.mateball.domain.group.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.domain.group.api.dto.*;
import at.mateball.domain.group.core.service.GroupV3Service;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @Operation(summary = "매칭 경기 화면 매칭 그룹 리스트 조회 api")
    public ResponseEntity<MateballResponse<GroupMatchRes>> getAvailableMatches(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long gameId
    ) {
        Long userId = customUserDetails.getUserId();

        GroupMatchRes result = groupV3Service.getGroupMatches(userId, gameId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));

    }

    @GetMapping("/create")
    @Operation(summary = "생성한 매칭 리스트 조회 api")
    public ResponseEntity<MateballResponse<CreateGroupListRes>> getCreateGroupList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();

        CreateGroupListRes result = groupV3Service.getCreateGroupList(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @GetMapping("/match/members/{matchId}")
    @Operation(summary = "매칭된 그룹원 리스트 조회 api")
    public ResponseEntity<MateballResponse<GroupMatchMemberListRes>> getMatchGroupMembers(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        GroupMatchMemberListRes result = groupV3Service.getMatchGroupMembers(userId, matchId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, result));
    }

    @GetMapping("request")
    @Operation(summary = "요청한 매칭 리스트 조회 api")
    public ResponseEntity<MateballResponse<RequestGroupListRes>> getRequestGroupList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();

        RequestGroupListRes ressult = groupV3Service.getRequestGroupList(userId);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, ressult));
    }

    @PostMapping("/match")
    @Operation(summary = "매칭 생성 api")
    public ResponseEntity<MateballResponse<?>> createMatch(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateMatchV3Req createMatchReq
    ) {
        Long userId = customUserDetails.getUserId();

        CreateMatchRes createMatchRes = groupV3Service.createMatch(userId, createMatchReq.gameId(), createMatchReq.matchType());

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, createMatchRes));
    }

    @PostMapping("/match-request/{matchId}")
    @Operation(summary = "매칭 요청 api")
    public ResponseEntity<MateballResponse<?>> createRequest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        groupV3Service.createRequest(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.CREATED));
    }

    @PatchMapping("/match-accept/{matchId}")
    @Operation(summary = "요청 수락 api")
    public ResponseEntity<MateballResponse<?>> permitRequest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @NotNull @PathVariable Long matchId
    ) {
        Long userId = customUserDetails.getUserId();

        groupV3Service.permitRequest(userId, matchId);

        return ResponseEntity.ok(MateballResponse.successWithNoData(SuccessCode.NO_CONTENT));
    }
}
