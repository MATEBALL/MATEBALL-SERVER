package at.mateball.domain.user.core.service;

import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.user.api.dto.request.EditUserInfoReq;
import at.mateball.domain.user.api.dto.request.UserInfoV2Req;
import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.UserInfoField;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.domain.user.core.validator.IntroductionValidator;
import at.mateball.domain.user.core.validator.NicknameValidator;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static at.mateball.exception.code.BusinessErrorCode.*;

@Service
public class UserV2Service {
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    private static final Integer LIMIT_AGE = 19;
    private static final Integer MIN_INTRODUCTION_LENGTH = 1;
    private static final Integer MAX_INTRODUCTION_LENGTH = 50;
    private static final String DEFAULT_PROFILE_IMAGE_URL =
            "https://mateball-file.s3.ap-northeast-2.amazonaws.com/profile.jpg";

    public UserV2Service(UserRepository userRepository, AlarmService alarmService) {
        this.userRepository = userRepository;
        this.alarmService = alarmService;
    }

    public InfoCheckRes getInfoCheck(Long userId) {
        return userRepository.infoCheck(userId);
    }

    @Transactional
    public void updateHasAccepted(Long userId, boolean hasAccepted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        if (user.isHasAccepted() == hasAccepted) {
            return;
        }

        user.updateHasAccepted(hasAccepted);
    }

    @Transactional
    public void createUserInfo(Long userId, @Valid UserInfoV2Req req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        if (user.getNickname() != null
                || user.getIntroduction() != null
                || user.getBirthYear() != null
                || user.getGender() != null) {
            throw new BusinessException(DUPLICATED_INFO);
        }

        validateNickname(req.nickname());
        validateIntroduction(req.introduction());
        validateAge(req.birthYear());

        user.updateNickname(req.nickname());
        user.updateIntroduction(req.introduction());
        user.updateGenderAndBirthYear(Gender.fromLabel(req.gender()), req.birthYear());
        user.updateProfileImage(DEFAULT_PROFILE_IMAGE_URL);
    }

    @Transactional
    public void editUserInfo(Long userId, @Valid EditUserInfoReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        UserInfoField field = UserInfoField.fromLabel(req.field());
        switch (field) {
            case NICKNAME -> {
                validateNickname(req.value());
                user.updateNickname(req.value());
            }
            case INTRODUCTION -> {
                validateIntroduction(req.value());
                user.updateIntroduction(req.value());
            }
            default -> throw new BusinessException(BAD_REQUEST_ENUM);
        }
    }

    private void validateNickname(String nickname) {
        NicknameValidator.validate(nickname);
        checkIsNicknameExists(nickname);
    }

    private void validateIntroduction(String introduction) {
        IntroductionValidator.validate(introduction);
        if (introduction == null || introduction.isBlank()
                || introduction.length() < MIN_INTRODUCTION_LENGTH
                || introduction.length() > MAX_INTRODUCTION_LENGTH) {
            throw new BusinessException(BusinessErrorCode.INVALID_INTRODUCTION_LENGTH);
        }
    }

    private void validateAge(int birthYear) {
        int age = LocalDate.now().getYear() - birthYear;
        if (age < LIMIT_AGE) {
            throw new BusinessException(AGE_NOT_APPROPRIATE);
        }
    }

    public void checkIsNicknameExists(@NotBlank String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        }
    }
}
