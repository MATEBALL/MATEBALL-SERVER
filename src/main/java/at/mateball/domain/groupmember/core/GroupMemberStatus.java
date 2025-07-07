package at.mateball.domain.groupmember.core;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GroupMemberStatus {
    PENDING_REQUEST(1, "요청대기중"),
    NEW_REQUEST(2, "새요청"),
    AWAITING_APPROVAL(3, "승인대기중"),
    APPROVED(4, "승인 완료"),
    MATCHED(5, "매칭 완료"),
    MATCH_FAILED(6, "매칭 실패");

    private final int value;
    private final String label;

    GroupMemberStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static GroupMemberStatus from(int value) {
        return Arrays.stream(values())
                .filter(g -> g.value == value)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
