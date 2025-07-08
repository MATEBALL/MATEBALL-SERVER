package at.mateball.domain.user.core.repository;

import at.mateball.domain.user.api.dto.response.UserInformationRes;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryCustom {
    UserInformationRes findUserInformation(Long userId);
}
