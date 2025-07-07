package at.mateball.domain.team.core;

import lombok.Getter;

@Getter
public enum Team {
    KIA(1),
    SAMSUNG(2),
    LG(3),
    DOOSAN(4),
    KT(5),
    SSG(6),
    LOTTE(7),
    HANWHA(8),
    NC(9),
    KIWOOM(10),
    NONE(11);

    private final int value;

    Team(int status) {
        this.value = status;
    }
}
