package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.team.core.TeamName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchRequirementService {
    private final MatchRequirementRepository matchRequirementRepository;

    public MatchRequirementService(
            MatchRequirementRepository matchRequirementRepository) {
        this.matchRequirementRepository = matchRequirementRepository;
    }

    public List<MatchingScoreDto> getMatchings(Long userId) {
        return matchRequirementRepository.findMatchingUsers(userId);
    }

    @Transactional
    public void setMatchRequirement(Long userId, String team, String teamAllowed, String style, String genderPreference) {
        MatchRequirement matchRequirement = matchRequirementRepository.findUserMatchRequirement(userId);

        matchRequirement.updateAll(
                TeamName.fromLabel(team).getValue(),
                teamAllowed == null ? TeamAllowed.NO_PREFERENCE.getValue() : TeamAllowed.fromLabel(teamAllowed).getValue(),
                Style.fromLabel(style).getValue(),
                Gender.fromLabel(genderPreference).getValue()
        );
    }
}
