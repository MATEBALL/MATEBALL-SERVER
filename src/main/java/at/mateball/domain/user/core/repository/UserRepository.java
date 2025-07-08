package at.mateball.domain.user.core.repository;

import at.mateball.domain.user.core.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByKakaoUserId(Long kakaoUserId);

    boolean existsByNickname(String nickname);
}
