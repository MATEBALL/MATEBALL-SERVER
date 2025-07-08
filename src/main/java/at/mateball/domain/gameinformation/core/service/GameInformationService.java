package at.mateball.domain.gameinformation.core.service;

import at.mateball.domain.gameinformation.api.dto.response.GameInformationRes;
import at.mateball.domain.gameinformation.api.dto.response.GameInformationsRes;
import at.mateball.domain.gameinformation.core.GameInformation;
import at.mateball.domain.gameinformation.core.repository.GameInformationRepository;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static at.mateball.exception.code.BusinessErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameInformationService {
    private final UserRepository userRepository;
    private final GameInformationRepository gameInformationRepository;

    public GameInformationsRes getGameInformation(Long userId, LocalDate date) {
        findUser(userId);

        List<GameInformationRes> list = gameInformationRepository.findByGameDate(date)
                .stream()
                .map(GameInformationRes::from)
                .toList();

        return new GameInformationsRes(list);
    }

    public User findUser(final Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new BusinessException(USER_NOT_FOUND));
    }
}
