package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchRequirementService {
    private final MatchRequirementRepository matchRequirementRepository;

    public MatchRequirementService(MatchRequirementRepository matchRequirementRepository) {
        this.matchRequirementRepository = matchRequirementRepository;
    }
}
