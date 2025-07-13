package at.mateball.domain.user.core.service;

import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.user.api.dto.request.UserInfoReq;
import at.mateball.domain.user.api.dto.response.KaKaoInformationRes;
import at.mateball.domain.user.api.dto.response.UserInformationRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.domain.user.core.validator.NicknameValidator;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static at.mateball.exception.code.BusinessErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;

    public KaKaoInformationRes getKakaoInformation(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        int currentYear = LocalDateTime.now().getYear();
        int birthYear = user.getBirthYear();

        final int LIMIT_AGE = 19;
        int age = currentYear - birthYear + 1;
        if (age < LIMIT_AGE) {
            throw new BusinessException(AGE_NOT_APPROPRIATE);
        }

        String gender = Gender.from(user.getGender()).getLabel();

        return new KaKaoInformationRes(birthYear, gender);
    }

    @Transactional
    public void updateNickname(final Long userId, final String updatedNickname) {
        NicknameValidator.validate(updatedNickname);

        if (userRepository.existsByNickname(updatedNickname)) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        }

        User user = findUser(userId);
        user.updateNickname(updatedNickname);
    }

    public UserInformationRes getUserInformation(Long userId) {
        userRepository.getUser(userId);

        return userRepository.findUserInformation(userId);
    }

    public User findUser(final Long userId) {
        return userRepository.getUser(userId).orElseThrow(()
                -> new BusinessException(USER_NOT_FOUND));
    }


    @Transactional
    public void createUserInfo(Long userId, String gender, int birthYear) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        int currentYear = LocalDate.now().getYear();
        int age = currentYear - birthYear;

        if (age < 19) {
            throw new BusinessException(AGE_NOT_APPROPRIATE);
        }
        Gender genderStatus = Gender.fromLabel(gender);

        user.updateProfileImage("https://mateball-file.s3.ap-northeast-2.amazonaws.com/profile.jpg");
        user.updateIntroduction("메잇볼이 선택한 너");
        user.updateGenderAndBirthYear(genderStatus, birthYear);
    }
}
