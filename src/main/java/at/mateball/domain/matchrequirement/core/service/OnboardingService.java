package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.user.core.service.UserV3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final UserV3Service userV3Service;
    private final MatchRequirementV3Service matchRequirementV3Service;

    @Transactional
    public void setOnboardingInformation(Long userId, String team, String teamAllowed, String style, int avgSeason) {
        userV3Service.setAvgSeason(userId, avgSeason);
        matchRequirementV3Service.setMatchRequirement(userId, team, teamAllowed, style);
    }
}
