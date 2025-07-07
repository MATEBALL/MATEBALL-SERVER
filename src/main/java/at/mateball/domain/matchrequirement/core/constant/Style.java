package at.mateball.domain.matchrequirement.core.constant;

import lombok.Getter;

@Getter
public enum Style {
    PASSIONATE_SUPPORTER(1),
    FOCUSED_VIEWER(2),
    FOODIE_VIEWER(3);

    private final int value;

    Style(int status) {
        this.value = status;
    }
}
