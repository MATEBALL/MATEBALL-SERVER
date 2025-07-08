package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static at.mateball.exception.code.BusinessErrorCode.USER_NOT_FOUND;

@Service
public class MatchRequirementService {
    private final UserRepository userRepository;
    private final MatchRequirementRepository matchRequirementRepository;

    public MatchRequirementService(
            UserRepository userRepository,
            MatchRequirementRepository matchRequirementRepository) {
        this.userRepository = userRepository;
        this.matchRequirementRepository = matchRequirementRepository;
    }

    public List<MatchingScoreDto> getMatchings(Long userId) {
        return matchRequirementRepository.findMatchingUsers(userId);
    }

    @Transactional
    public void setMatchRequirement(Long userId, String team, String teamAllowed, String style, String genderPreference) {

        findUser(userId);
        MatchRequirement matchRequirement = matchRequirementRepository.findUserMatchRequirement(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        matchRequirement.updateAll(
                TeamName.fromLabel(team).getValue(),
                teamAllowed == null ? TeamAllowed.NO_PREFERENCE.getValue() : TeamAllowed.fromLabel(teamAllowed).getValue(),
                Style.fromLabel(style).getValue(),
                Gender.fromLabel(genderPreference).getValue()
        );
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new BusinessException(USER_NOT_FOUND));
    }
}
