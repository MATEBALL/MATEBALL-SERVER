package at.mateball.domain.user.core.repository;

import at.mateball.domain.user.api.dto.response.UserInformationRes;
import at.mateball.domain.user.core.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryCustom {
    UserInformationRes findUserInformation(Long userId);

    boolean existsByNickname(String nickname);

    Optional<User> getUser(final Long userId);
}
