package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.domain.matchrequirement.core.service.MatchRequirementService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchRequirementController {
    private final MatchRequirementService matchRequirementService;

    public MatchRequirementController(MatchRequirementService matchRequirementService) {
        this.matchRequirementService = matchRequirementService;
    }
}
