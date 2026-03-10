package at.mateball.domain.group.core.calculator.common;

import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class GroupProfileImageResolver {

    private final FileStorage fileStorage;

    public String resolve(String profileImageKey) {
        return fileStorage.getImageUrl(profileImageKey);
    }
}
