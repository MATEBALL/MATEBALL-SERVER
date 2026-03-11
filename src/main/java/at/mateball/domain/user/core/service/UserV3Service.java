package at.mateball.domain.user.core.service;

import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static at.mateball.exception.code.BusinessErrorCode.INVALID_AVG_SEASON;
import static at.mateball.exception.code.BusinessErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserV3Service {
    private final UserRepository userRepository;
    private static final int MIN_AVG_SEASON = 0;
    private static final int MAX_AVG_SEASON = 999;

    public void setAvgSeason(final Long userId, final int avgSeason) {
        if (avgSeason > MAX_AVG_SEASON || avgSeason < MIN_AVG_SEASON) {
            throw new BusinessException(INVALID_AVG_SEASON);
        }

        findUser(userId).updateAvgSeason(avgSeason);
    }

    private User findUser(final Long userId) {
        return userRepository.getUser(userId).orElseThrow(()
                -> new BusinessException(USER_NOT_FOUND));
    }
}
