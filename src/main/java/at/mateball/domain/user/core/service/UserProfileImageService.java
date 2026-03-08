package at.mateball.domain.user.core.service;

import at.mateball.storage.dto.ImageUploadRes;
import at.mateball.storage.S3Service;
import at.mateball.domain.user.api.dto.response.ProfileImageUploadRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileImageService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public ProfileImageUploadRes uploadProfileImage(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        ImageUploadRes uploadResult = s3Service.uploadProfileImage(file);

        user.updateProfileImageKey(uploadResult.objectKey());

        String profileImageUrl = s3Service.getImageUrl(uploadResult.objectKey());

        return new ProfileImageUploadRes(
                user.getProfileImageKey(),
                profileImageUrl
        );
    }

    // objectKey 기반 구조로 변경 예정
    public String getProfileImageUrl(User user) {
        if (user.getProfileImageKey() == null || user.getProfileImageKey().isBlank()) {
            return User.DEFAULT_PROFILE_IMAGE_URL;
        }

        return s3Service.getImageUrl(user.getProfileImageKey());
    }
}