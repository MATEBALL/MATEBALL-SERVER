package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.api.dto.MatchingScoreDto;
import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchRequirementService {
    private final MatchRequirementRepository matchRequirementRepository;

    public MatchRequirementService(MatchRequirementRepository matchRequirementRepository) {
        this.matchRequirementRepository = matchRequirementRepository;
    }

    public List<MatchingScoreDto> getMatchings(Long userId) {
        return matchRequirementRepository.findMatchingUsers(userId);
    }
}
