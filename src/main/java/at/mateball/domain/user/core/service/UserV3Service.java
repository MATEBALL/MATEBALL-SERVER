package at.mateball.domain.user.core.service;

import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static at.mateball.exception.code.BusinessErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserV3Service {
    private final UserRepository userRepository;

    public void setAvgSeason(final Long userId, final int avgSeason) {
        findUser(userId).updateAvgSeason(avgSeason);
    }

    private User findUser(final Long userId) {
        return userRepository.getUser(userId).orElseThrow(()
                -> new BusinessException(USER_NOT_FOUND));
    }

    public int getAvgSeason(final Long userId) {
        return findUser(userId).getAvgSeason();
    }

    @Transactional
    public void clearOnboardingInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        user.clearOnboardingInfo();
    }
}
