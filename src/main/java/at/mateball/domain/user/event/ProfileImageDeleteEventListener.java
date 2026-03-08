package at.mateball.domain.user.event;

import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
class ProfileImageDeleteEventListener {

    private final FileStorage fileStorage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProfileImageDeleteEvent event) {
        try {
            fileStorage.deleteObject(event.objectKey());
        } catch (Exception e) {
            log.warn("기존 프로필 이미지 삭제 실패. objectKey={}", event.objectKey(), e);
        }
    }
}
