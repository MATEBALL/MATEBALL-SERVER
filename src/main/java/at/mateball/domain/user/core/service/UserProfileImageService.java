package at.mateball.domain.user.core.service;

import at.mateball.domain.user.api.dto.response.ProfileImageUpdateRes;
import at.mateball.storage.FileStorage;
import at.mateball.storage.dto.ImageUploadRes;
import at.mateball.domain.user.api.dto.response.ProfileImageUploadRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileImageService {

    private final UserRepository userRepository;
    private final FileStorage fileStorage;

    @Transactional
    public ProfileImageUploadRes uploadProfileImage(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        ImageUploadRes uploadResult = fileStorage.uploadProfileImage(file);

        user.updateProfileImageKey(uploadResult.objectKey());

        String profileImageUrl = fileStorage.getImageUrl(uploadResult.objectKey());

        return new ProfileImageUploadRes(
                user.getProfileImageKey(),
                profileImageUrl
        );
    }

    @Transactional
    public ProfileImageUpdateRes updateProfileImage(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        String oldProfileImageKey = user.getProfileImageKey();

        ImageUploadRes uploadRes = fileStorage.uploadProfileImage(file);
        String newProfileImageKey = uploadRes.objectKey();

        user.updateProfileImageKey(newProfileImageKey);

        deleteOldProfileImageQuietly(oldProfileImageKey);

        String imageUrl = fileStorage.getImageUrl(newProfileImageKey);

        return new ProfileImageUpdateRes(imageUrl);
    }

    // objectKey 기반 구조로 변경 예정
    public String getProfileImageUrl(User user) {
        if (user.getProfileImageKey() == null || user.getProfileImageKey().isBlank()) {
            return User.DEFAULT_PROFILE_IMAGE_URL;
        }

        return fileStorage.getImageUrl(user.getProfileImageKey());
    }

    private void deleteOldProfileImageQuietly(String oldProfileImageKey) {
        if (oldProfileImageKey == null || oldProfileImageKey.isBlank()) {
            return;
        }

        try {
            fileStorage.deleteObject(oldProfileImageKey);
        } catch (Exception e) {
            log.warn("기존 프로필 이미지 삭제 실패. objectKey={}", oldProfileImageKey, e);
        }
    }
}