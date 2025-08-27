package at.mateball.domain.matchrequirement.core.service;


import at.mateball.domain.matchrequirement.api.dto.request.MatchRequirementReq;
import at.mateball.domain.matchrequirement.api.dto.response.MatchRequirementRes;
import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Gender;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.team.core.TeamName;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static at.mateball.exception.code.BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND;

@Service
public class MatchRequirementV2Service {
    private final MatchRequirementRepository matchRequirementRepository;

    public MatchRequirementV2Service(MatchRequirementRepository matchRequirementRepository) {
        this.matchRequirementRepository = matchRequirementRepository;
    }

    public MatchRequirementRes getMatchRequirement(Long userId) {
        MatchRequirement requirement = Optional.ofNullable(matchRequirementRepository.findUserMatchRequirement(userId))
                .orElseThrow(() -> new BusinessException(MATCH_REQUIREMENT_NOT_FOUND));

        return new MatchRequirementRes(
                TeamName.from(requirement.getTeam()).getLabel(),
                TeamAllowed.from(requirement.getTeamAllowed()).getLabel(),
                Style.from(requirement.getStyle()).getLabel(),
                Gender.from(requirement.getGenderPreference()).getLabel()
        );
    }

    @Transactional
    public void setMatchRequirement(Long userId, MatchRequirementReq req) {
        MatchRequirement matchRequirement = matchRequirementRepository.findUserMatchRequirement(userId);

        if (req.team() != null) {
            TeamName selectedTeam = TeamName.fromLabel(req.team());
            matchRequirement.updateTeam(selectedTeam.getValue());

            if (req.teamAllowed() != null) {
                TeamAllowed selectedAllowed = TeamAllowed.fromLabel(req.teamAllowed());
                if (selectedTeam == TeamName.NONE && selectedAllowed != TeamAllowed.NO_PREFERENCE) {
                    throw new BusinessException(BusinessErrorCode.INVALID_TEAM_ALLOWED);
                }
                matchRequirement.updateTeamAllowed(selectedAllowed.getValue());
            }
        } else if (req.teamAllowed() != null) {
            TeamAllowed selectedAllowed = TeamAllowed.fromLabel(req.teamAllowed());
            matchRequirement.updateTeamAllowed(selectedAllowed.getValue());
        }

        if (req.style() != null) {
            matchRequirement.updateStyle(Style.fromLabel(req.style()).getValue());
        }
        if (req.genderPreference() != null) {
            matchRequirement.updateGenderPreference(Gender.fromLabel(req.genderPreference()).getValue());
        }
    }
}
