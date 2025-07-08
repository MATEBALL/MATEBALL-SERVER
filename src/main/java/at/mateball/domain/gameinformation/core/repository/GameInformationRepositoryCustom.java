package at.mateball.domain.gameinformation.core.repository;

import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.user.core.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameInformationRepositoryCustom {
    List<GameInformation> findByGameDate(LocalDate gameDate);
}
