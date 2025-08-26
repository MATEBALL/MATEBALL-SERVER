package at.mateball.domain.user.core.service;

import at.mateball.domain.user.api.dto.response.CheckUserV2Res;
import at.mateball.domain.user.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserV2Service {
    private final UserRepository userRepository;

    public UserV2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CheckUserV2Res getInfoCheck(Long userId) {
        return userRepository.infoCheck(userId);
    }
}
