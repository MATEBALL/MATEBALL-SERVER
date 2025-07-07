package at.mateball.domain.matchrequirement.core.constant;

import lombok.Getter;

@Getter
public enum Gender {
    MALE(1),
    FEMALE(2),
    NO_PREFERENCE(3);

    private final int value;

    Gender(int status) {
        this.value = status;
    }
}
