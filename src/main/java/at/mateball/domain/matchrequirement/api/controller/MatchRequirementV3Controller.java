package at.mateball.domain.matchrequirement.api.controller;

import at.mateball.domain.matchrequirement.core.service.MatchRequirementV3Service;
import at.mateball.domain.user.core.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/users/match-condition")
public class MatchRequirementV3Controller {
    private final MatchRequirementV3Service matchRequirementV3Service;

    public MatchRequirementV3Controller(MatchRequirementV3Service matchRequirementV3Service, UserRepository userRepository) {
        this.matchRequirementV3Service = matchRequirementV3Service;
    }


}
