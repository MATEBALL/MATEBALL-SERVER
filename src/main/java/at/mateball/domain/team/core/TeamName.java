package at.mateball.domain.team.core;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TeamName {
    KIA(1, "KIA 타이거즈"),
    SAMSUNG(2, "삼성 라이온즈"),
    LG(3, "LG 트윈스"),
    DOOSAN(4, "두산 베어스"),
    KT(5, "KT 위즈"),
    SSG(6, "SSG 랜더스"),
    LOTTE(7, "롯데 자이언츠"),
    HANWHA(8, "한화 이글스"),
    NC(9, "NC 다이노스"),
    KIWOOM(10, "키움 히어로즈"),
    NONE(11, "응원하는 팀이 없어요.");

    private final int value;
    private final String label;

    TeamName(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static TeamName from(int value) {
        return Arrays.stream(values())
                .filter(g -> g.value == value)
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }

    public static TeamName fromLabel(String label) {
        return Arrays.stream(values())
                .filter(e -> e.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.BAD_REQUEST_ENUM));
    }
}
