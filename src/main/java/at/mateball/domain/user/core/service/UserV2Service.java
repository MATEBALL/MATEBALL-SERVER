package at.mateball.domain.user.core.service;

import at.mateball.domain.alarm.core.service.AlarmService;
import at.mateball.domain.matchrequirement.core.constant.Gender;
<<<<<<< HEAD
import at.mateball.domain.user.api.dto.request.EditUserInfoReq;
=======
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)
import at.mateball.domain.user.api.dto.request.UserInfoV2Req;
import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.UserInfoField;
import at.mateball.domain.user.core.repository.UserRepository;
<<<<<<< HEAD
import at.mateball.domain.user.core.validator.IntroductionValidator;
import at.mateball.domain.user.core.validator.NicknameValidator;
import at.mateball.exception.BusinessException;
=======
import at.mateball.domain.user.core.validator.NicknameValidator;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static at.mateball.exception.code.BusinessErrorCode.*;

@Service
public class UserV2Service {
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    private static final Integer LIMIT_AGE = 19;
<<<<<<< HEAD
=======
    private static final Integer MIN_INTRODUCTION_LENGTH = 1;
    private static final Integer MAX_INTRODUCTION_LENGTH = 50;
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)
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

<<<<<<< HEAD
        if (user.getNickname() != null || user.getIntroduction() != null || user.getBirthYear() != null || user.getGender() != null) {
            throw new BusinessException(DUPLICATED_INFO);
        }

=======
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)
        validateNickname(req.nickname());
        validateIntroduction(req.introduction());
        validateAge(req.birthYear());

        user.updateNickname(req.nickname());
        user.updateIntroduction(req.introduction());
        user.updateGenderAndBirthYear(Gender.fromLabel(req.gender()), req.birthYear());
        user.updateProfileImage(DEFAULT_PROFILE_IMAGE_URL);
    }

<<<<<<< HEAD
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

=======
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)
    private void validateNickname(String nickname) {
        NicknameValidator.validate(nickname);
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        }
    }

    private void validateIntroduction(String introduction) {
<<<<<<< HEAD
        IntroductionValidator.validate(introduction);
=======
        if (introduction == null || introduction.isBlank() || introduction.length() < MIN_INTRODUCTION_LENGTH || introduction.length() > MAX_INTRODUCTION_LENGTH) {
            throw new BusinessException(BusinessErrorCode.INVALID_INTRODUCTION_LENGTH);
        }
>>>>>>> 29c4d6b ([feat/#152] 온보딩 회원 정보 설정 api 구현)
    }

    private void validateAge(int birthYear) {
        int age = LocalDate.now().getYear() - birthYear;
        if (age < LIMIT_AGE) {
            throw new BusinessException(AGE_NOT_APPROPRIATE);
        }
    }
}
