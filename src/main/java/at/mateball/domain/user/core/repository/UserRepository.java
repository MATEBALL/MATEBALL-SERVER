package at.mateball.domain.user.core.repository;

import at.mateball.domain.user.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByKakaoUserId(Long kakaoUserId);
    @Query("select u.nickname from User u where u.id = :id")
    String findNicknameById(@Param("id") Long id);
}
