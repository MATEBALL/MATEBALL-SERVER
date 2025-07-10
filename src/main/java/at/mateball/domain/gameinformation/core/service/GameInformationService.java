package at.mateball.domain.gameinformation.core.service;

import at.mateball.domain.gameinformation.api.dto.response.GameInformationRes;
import at.mateball.domain.gameinformation.api.dto.response.GameInformationsRes;
import at.mateball.domain.gameinformation.core.repository.GameInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static at.mateball.domain.group.core.validator.DateValidator.validate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameInformationService {
    private final GameInformationRepository gameInformationRepository;

    public GameInformationsRes getGameInformation(Long userId, LocalDate date) {
        validate(date);

        List<GameInformationRes> list = gameInformationRepository.findByGameDate(date)
                .stream()
                .map(GameInformationRes::from)
                .toList();

        return new GameInformationsRes(list);
    }
}
