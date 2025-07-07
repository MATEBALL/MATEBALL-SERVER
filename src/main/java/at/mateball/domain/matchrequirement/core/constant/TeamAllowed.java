package at.mateball.domain.matchrequirement.core.constant;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TeamAllowed {
    SAME_TEAM_ONLY(1, "같은 팀 메이트와 보고 싶어요"),
    NO_PREFERENCE(2, "상관 없어요");

    private final int value;
    private final String label;

    TeamAllowed(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static TeamAllowed from(int value) {
        return Arrays.stream(values())
                .filter(g -> g.value == value)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
