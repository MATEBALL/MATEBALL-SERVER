package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.core.MatchRequirement;
import at.mateball.domain.matchrequirement.core.constant.Style;
import at.mateball.domain.matchrequirement.core.constant.TeamAllowed;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import at.mateball.domain.matchrequirement.core.repository.querydsl.MatchRequirementUpdateReq;
import at.mateball.domain.team.core.TeamName;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static at.mateball.exception.code.BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor

public class MatchRequirementV3Service {
    private final MatchRequirementRepository matchRequirementRepository;
    private final UserRepository userRepository;

    public void setMatchRequirement(Long userId, String team, String teamAllowed, String style) {
        User user = userRepository.getReferenceById(userId);
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
        matchRequirementRepository.save(matchRequirement);
    }

    public MatchRequirement getMatchRequirement(Long userId) {
        MatchRequirement matchRequirement = Optional.ofNullable(matchRequirementRepository.findUserMatchRequirement(userId))
                .orElseThrow(() -> new BusinessException(MATCH_REQUIREMENT_NOT_FOUND));

        if (matchRequirement.getTeam() == null
                || matchRequirement.getTeamAllowed() == null
                || matchRequirement.getStyle() == null) {
            throw new BusinessException(MATCH_REQUIREMENT_NOT_FOUND);
        }

        return matchRequirement;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        MatchRequirement matchRequirement = matchRequirementRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MATCH_REQUIREMENT_NOT_FOUND));

        matchRequirementRepository.delete(matchRequirement);
    }

    public void updateMatchRequirement(Long userId, MatchRequirementUpdateReq req) {
        MatchRequirement matchRequirement =
                matchRequirementRepository.findUserMatchRequirement(userId);

        TeamName currentTeam = TeamName.from(matchRequirement.getTeam());
        TeamAllowed currentAllowed = TeamAllowed.from(matchRequirement.getTeamAllowed());

        TeamName newTeam = req.team() != null
                ? TeamName.fromLabel(req.team())
                : currentTeam;

        TeamAllowed newAllowed = req.teamAllowed() != null
                ? TeamAllowed.fromLabel(req.teamAllowed())
                : currentAllowed;

        if (newTeam == TeamName.NONE && newAllowed != TeamAllowed.NO_PREFERENCE) {
            throw new BusinessException(BusinessErrorCode.INVALID_TEAM_ALLOWED);
        }

        if (req.team() != null) {
            matchRequirement.updateTeam(newTeam.getValue());
        }

        if (req.teamAllowed() != null) {
            matchRequirement.updateTeamAllowed(newAllowed.getValue());
        }

        if (req.style() != null) {
            matchRequirement.updateStyle(Style.fromLabel(req.style()).getValue());
        }
    }
}
