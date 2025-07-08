package at.mateball.domain.groupmember.api.controller;

import at.mateball.domain.groupmember.core.service.GroupMemberService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }
}
