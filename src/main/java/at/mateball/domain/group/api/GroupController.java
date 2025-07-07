package at.mateball.domain.group.api;

import at.mateball.domain.group.core.service.GroupService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
}
