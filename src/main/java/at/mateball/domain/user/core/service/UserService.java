package at.mateball.domain.user.core.service;

import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.user.api.dto.response.KaKaoInformationRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static at.mateball.exception.code.BusinessErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public KaKaoInformationRes getKakaoInformation(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        int currentYear = LocalDateTime.now().getYear();
        int birthYear = user.getBirthYear();

        int LIMIT_AGE = 19;
        int age = currentYear - birthYear + 1;
        if (age < LIMIT_AGE) {
            throw new BusinessException(AGE_NOT_APPROPRIATE);
        }

        String gender = Gender.from(user.getGender()).getLabel();

        return new KaKaoInformationRes(birthYear, gender);
    }

    @Transactional
    public void updateNickname(final Long userId, final String updatedNickname) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new BusinessException(USER_NOT_FOUND));

        if (userRepository.existsByNickname(updatedNickname)) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        }

        user.updateNickname(updatedNickname);
    }
}
