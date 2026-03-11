package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.service.UserV3Service;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class MatchRequirementV3Service {
    private final MatchRequirementRepository matchRequirementRepository;
    private final UserV3Service userV3Service;
    private final EntityManager entityManager;

    @Transactional
    public void setAvgSeasonAndMatchRequirement(Long userId, String team, String teamAllowed, String style, int avgSeason) {
        userV3Service.setAvgSeason(userId, avgSeason);
        setMatchRequirement(userId, team, teamAllowed, style);
    }

    public void setMatchRequirement(Long userId, String team, String teamAllowed, String style) {
        User user = entityManager.getReference(User.class, userId);
        MatchRequirement matchRequirement = matchRequirementRepository.findUserMatchRequirement(userId);

        if (matchRequirement != null) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_MATCH_REQUIREMENT);
        }

        matchRequirement = new MatchRequirement(
                user,
                TeamName.fromLabel(team).getValue(),
                teamAllowed == null ? TeamAllowed.NO_PREFERENCE.getValue() : TeamAllowed.fromLabel(teamAllowed).getValue(),
                Style.fromLabel(style).getValue()
        );
        entityManager.persist(matchRequirement);
    }
}
