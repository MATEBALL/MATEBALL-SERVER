package at.mateball.domain.matchrequirement.core.uril;

import java.time.LocalDateTime;

public class MatchingRateCalculator {
    private MatchingRateCalculator() {

    }

    public static int calculateAge(int birthYear) {
        return LocalDateTime.now().getYear() - birthYear + 1;
    }

    public static boolean isAgeDifferenceAcceptableABoolean(int birthYearA, int birthYearB) {
        return Math.abs(calculateAge(birthYearA) - calculateAge(birthYearB)) <= 5;
    }

    public static int calculateTeamScore(int teamA, int allowA, int teamB, int allowB) {
        if (allowA == 2 && allowB == 2) {
            return 40;
        }

        if ((allowA == 1 || allowB == 1) && teamA == teamB && teamA != 11) {
            return 40;
        }

        if ((teamA == 11 || teamB == 11) && allowA == 2 && allowB == 2) {
            return 40;
        }

        return 0;
    }

    public static int calculateGenderScore(String genderA, int prefA, String genderB, int prefB) {
        boolean aOK = prefA == 3 || (prefA == 1 && "M".equals(genderB)) || (prefA == 2 && "F".equals(genderB));
        boolean bOk = prefB == 3 || (prefB == 1 && "M".equals(genderA)) || (prefB == 2 && "F".equals(genderA));
        return aOK && bOk ? 35 : 0;
    }

    public static int calculateStyleScore(int styleA, int styleB) {
        if (styleA == styleB) {
            return 25;
        }

        if ((styleA == 1 && styleB == 3) || (styleA == 3 && styleB == 1)) {
            return 20;
        }

        return 10;
    }
}
