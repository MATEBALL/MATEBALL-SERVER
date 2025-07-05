package at.mateball.domain.auth.core.repository;

import at.mateball.domain.auth.core.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
