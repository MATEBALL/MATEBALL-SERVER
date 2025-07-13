package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchRequirementService {
    private final MatchRequirementRepository matchRequirementRepository;

    private final EntityManager entityManager;

    public MatchRequirementService(MatchRequirementRepository matchRequirementRepository, EntityManager entityManager) {
        this.matchRequirementRepository = matchRequirementRepository;
        this.entityManager = entityManager;
    }

    public List<MatchingScoreDto> getMatchings(Long userId) {
        return matchRequirementRepository.findMatchingUsers(userId);
    }

    @Transactional
    public void setMatchRequirement(Long userId, String team, String teamAllowed, String style, String genderPreference) {
        User user = entityManager.getReference(User.class, userId);
        MatchRequirement matchRequirement = matchRequirementRepository.findUserMatchRequirement(userId);

        if (matchRequirement == null) {
            matchRequirement = new MatchRequirement(
                    user,
                    TeamName.fromLabel(team).getValue(),
                    teamAllowed == null ? TeamAllowed.NO_PREFERENCE.getValue() : TeamAllowed.fromLabel(teamAllowed).getValue(),
                    Style.fromLabel(style).getValue(),
                    Gender.fromLabel(genderPreference).getValue()
            );
            entityManager.persist(matchRequirement);
        } else {
            matchRequirement.updateAll(
                    TeamName.fromLabel(team).getValue(),
                    teamAllowed == null ? TeamAllowed.NO_PREFERENCE.getValue() : TeamAllowed.fromLabel(teamAllowed).getValue(),
                    Style.fromLabel(style).getValue(),
                    Gender.fromLabel(genderPreference).getValue()
            );
        }
    }
}
