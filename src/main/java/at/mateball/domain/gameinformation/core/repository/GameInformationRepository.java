package at.mateball.domain.gameinformation.core.repository;

import at.mateball.domain.gameinformation.core.GameInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GameInformationRepository extends JpaRepository<GameInformation, Long> {
    List<GameInformation> findByGameDate(LocalDate gameDate);
}
