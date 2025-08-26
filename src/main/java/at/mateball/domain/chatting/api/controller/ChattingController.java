package at.mateball.domain.chatting.api.controller;

import at.mateball.domain.chatting.core.service.ChattingV2Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/users/")
public class ChattingController {
    private final ChattingV2Service chattingV2Service;

    public ChattingController(ChattingV2Service chattingV2Service) {
        this.chattingV2Service = chattingV2Service;
    }
}
