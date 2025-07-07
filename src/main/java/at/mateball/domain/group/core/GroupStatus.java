package at.mateball.domain.group.core;

import lombok.Getter;

@Getter
public enum GroupStatus {
    PENDING(1),
    COMPLETED(2),
    FAILED(3);

    private final int value;

    GroupStatus(int status) {
        this.value = status;
    }
}
