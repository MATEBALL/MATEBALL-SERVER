package at.mateball.domain.groupmember.core;

import lombok.Getter;

@Getter
public enum GroupMemberStatus {
    PENDING_REQUEST(1),
    NEW_REQUEST(2),
    AWAITING_APPROVAL(3),
    APPROVED(4),
    MATCHED(5),
    MATCH_FAILED(6);

    private final int value;

    GroupMemberStatus(int status) {
        this.value = status;
    }
}
