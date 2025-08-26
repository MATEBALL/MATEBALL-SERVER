package at.mateball.domain.matchrequirement.core.service;


import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.api.dto.request.MatchRequirementReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchRequirementV2Service {
    private final MatchRequirementRepository matchRequirementRepository;

    public MatchRequirementV2Service(MatchRequirementRepository matchRequirementRepository) {
        this.matchRequirementRepository = matchRequirementRepository;
    }

    @Transactional
    public void setMatchRequirement(Long userId, MatchRequirementReq req) {
        MatchRequirement matchRequirement = matchRequirementRepository.findUserMatchRequirement(userId);

        if (req.team() != null) {
            matchRequirement.updateTeam(TeamName.fromLabel(req.team()).getValue());
        }
        if (req.teamAllowed() != null) {
            matchRequirement.updateTeamAllowed(TeamAllowed.fromLabel(req.teamAllowed()).getValue());
        }
        if (req.style() != null) {
            matchRequirement.updateStyle(Style.fromLabel(req.style()).getValue());
        }
        if (req.genderPreference() != null) {
            matchRequirement.updateGenderPreference(Gender.fromLabel(req.genderPreference()).getValue());
        }
    }
}
