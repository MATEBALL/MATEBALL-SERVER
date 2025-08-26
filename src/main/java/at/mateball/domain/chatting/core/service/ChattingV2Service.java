package at.mateball.domain.chatting.core.service;

import at.mateball.domain.chatting.core.Chatting;
import at.mateball.domain.chatting.core.repository.ChattingV2Repository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingV2Service {
    private final ChattingV2Repository chattingV2Repository;

    @Transactional
    public Chatting assignChatting() {
        Chatting chatting = chattingV2Repository.findFirstByIsUsedFalse()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.CHATTING_NOT_FOUND));

        chatting.updateIsUsedStatus();
        return chattingV2Repository.save(chatting);
    }
}
