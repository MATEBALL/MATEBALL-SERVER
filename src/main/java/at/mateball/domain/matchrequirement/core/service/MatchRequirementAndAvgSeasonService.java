package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.api.dto.response.MatchRequirementAndAvgSeasonRes;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.querydsl.MatchRequirementUpdateReq;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.service.UserV3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchRequirementAndAvgSeasonService {
    private final UserV3Service userV3Service;
    private final MatchRequirementV3Service matchRequirementV3Service;

    @Transactional
    public void setMatchRequirementAndAvgSeason(Long userId, String team, String teamAllowed, String style, int avgSeason) {
        userV3Service.setAvgSeason(userId, avgSeason);
        matchRequirementV3Service.setMatchRequirement(userId, team, teamAllowed, style);
    }

    public MatchRequirementAndAvgSeasonRes getMatchRequirementAndAvgSeason(Long userId) {
        int avgSeason = userV3Service.getAvgSeason(userId);
        MatchRequirement matchRequirement = matchRequirementV3Service.getMatchRequirement(userId);

        return new MatchRequirementAndAvgSeasonRes(
                TeamName.from(matchRequirement.getTeam()).getLabel(),
                TeamAllowed.from(matchRequirement.getTeamAllowed()).getLabel(),
                Style.from(matchRequirement.getStyle()).getLabel(),
                avgSeason
        );

    }

    @Transactional
    public void updateMatchRequirementAndAvgSeason(Long userId, MatchRequirementUpdateReq req) {
        userV3Service.updateAvgSeason(userId, req.avgSeason());
        matchRequirementV3Service.updateMatchRequirement(userId, req);
    }
}
