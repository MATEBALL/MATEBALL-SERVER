package at.mateball.domain.user.core.service;

import at.mateball.domain.user.api.dto.response.ProfileImageUpdateRes;
import at.mateball.domain.user.event.ProfileImageDeleteEvent;
import at.mateball.storage.FileStorage;
import at.mateball.storage.dto.ImageUploadRes;
import at.mateball.domain.user.api.dto.response.ProfileImageUploadRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileImageService {

    private final UserRepository userRepository;
    private final FileStorage fileStorage;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public ProfileImageUploadRes uploadProfileImage(Long userId, MultipartFile file) throws Exception {
        User user = getUser(userId);

        ImageUploadRes uploadResult = fileStorage.uploadProfileImage(file);
        String newProfileImageKey = uploadResult.objectKey();

        try {
            user.updateProfileImageKey(newProfileImageKey);

            String profileImageUrl = fileStorage.getImageUrl(newProfileImageKey);

            return new ProfileImageUploadRes(
                    user.getProfileImageKey(),
                    profileImageUrl
            );
        } catch (Exception e) {
            deleteNewProfileImageQuietly(newProfileImageKey);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ProfileImageUpdateRes updateProfileImage(Long userId, MultipartFile file) throws Exception {
        User user = getUser(userId);

        String oldProfileImageKey = user.getProfileImageKey();

        ImageUploadRes uploadRes = fileStorage.uploadProfileImage(file);
        String newProfileImageKey = uploadRes.objectKey();

        try {
            user.updateProfileImageKey(newProfileImageKey);

            if (hasText(oldProfileImageKey)) {
                eventPublisher.publishEvent(new ProfileImageDeleteEvent(oldProfileImageKey));
            }

            String imageUrl = fileStorage.getImageUrl(newProfileImageKey);
            return new ProfileImageUpdateRes(imageUrl);

        } catch (Exception e) {
            deleteNewProfileImageQuietly(newProfileImageKey);
            throw e;
        }
    }

    // objectKey 기반 구조로 변경 예정
    public String getProfileImageUrl(User user) {
        if (user.getProfileImageKey() == null || user.getProfileImageKey().isBlank()) {
            return User.DEFAULT_PROFILE_IMAGE_URL;
        }

        return fileStorage.getImageUrl(user.getProfileImageKey());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));
    }

    private void deleteNewProfileImageQuietly(String objectKey) {
        if (!hasText(objectKey)) {
            return;
        }

        try {
            fileStorage.deleteObject(objectKey);
        } catch (Exception e) {
            log.warn("보상 삭제 실패. newProfileImageKey={}", objectKey, e);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}