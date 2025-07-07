package at.mateball.domain.user.core.service;

import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static at.mateball.exception.code.BusinessErrorCode.DUPLICATED_NICKNAME;
import static at.mateball.exception.code.BusinessErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

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
