package at.mateball.domain.matchrequirement.core.constant;

import lombok.Getter;

@Getter
public enum TeamAllowed {
    SAME_TEAM_ONLY(1),
    NO_PREFERENCE(2);

    private final int value;

    TeamAllowed(int status) {
        this.value = status;
    }
}
