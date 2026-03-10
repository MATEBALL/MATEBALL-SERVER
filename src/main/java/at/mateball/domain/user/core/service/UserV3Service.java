package at.mateball.domain.user.core.service;

import at.mateball.domain.user.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserV3Service {
    private final UserRepository userRepository;

    public UserV3Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
