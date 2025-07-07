package at.mateball.domain.team.core;

import at.mateball.domain.group.core.GroupStatus;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TeamName {
    KIA(1, "KIA"),
    SAMSUNG(2, "삼성"),
    LG(3, "LG"),
    DOOSAN(4, "두산"),
    KT(5, "KT"),
    SSG(6, "SSG"),
    LOTTE(7, "롯데"),
    HANWHA(8, "한화"),
    NC(9, "NC"),
    KIWOOM(10, "키움"),
    NONE(11, "응원하는 팀이 없어요.");

    private final int value;
    private final String lkabel;

    TeamName(int value, String lkabel) {
        this.value = value;
        this.lkabel = lkabel;
    }

    public static TeamName from(int value) {
        return Arrays.stream(values())
                .filter(g -> g.value == value)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
