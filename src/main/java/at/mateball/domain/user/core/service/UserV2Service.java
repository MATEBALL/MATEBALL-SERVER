package at.mateball.domain.user.core.service;

import at.mateball.domain.user.api.dto.response.InfoCheckRes;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserV2Service {
    private final UserRepository userRepository;

    public UserV2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
