package at.mateball.domain.matchrequirement.core.service;

import at.mateball.domain.matchrequirement.core.repository.MatchRequirementRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class MatchRequirementV3Service {
    private final MatchRequirementRepository matchRequirementRepository;

    private final EntityManager entityManager;

    public MatchRequirementV3Service(MatchRequirementRepository matchRequirementRepository, EntityManager entityManager) {
        this.matchRequirementRepository = matchRequirementRepository;
        this.entityManager = entityManager;
    }


}
