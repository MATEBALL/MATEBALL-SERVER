package at.mateball.domain.user.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.security.CustomUserDetails;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.common.swagger.SwaggerResponseDescription;
import at.mateball.domain.user.api.dto.response.ProfileImageUploadRes;
import at.mateball.domain.user.core.service.UserProfileImageService;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/users")
public class UserImgController {

    private final UserProfileImageService userProfileImageService;

    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 업로드 api")
    public ResponseEntity<MateballResponse<?>> uploadProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("file") MultipartFile file
    ) throws Exception {
        Long userId = userDetails.getUserId();

        ProfileImageUploadRes data = userProfileImageService.uploadProfileImage(userId, file);

        return ResponseEntity.ok(MateballResponse.success(SuccessCode.OK, data));
    }
}