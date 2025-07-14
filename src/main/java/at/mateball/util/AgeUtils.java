package at.mateball.util;

import java.time.LocalDate;

public class AgeUtils {

    private AgeUtils() {
    }

    public static String calculateAge(int birthYear) {
        int currentYear = LocalDate.now().getYear();
        int age = currentYear - birthYear + 1;
        return age + "세";
    }
}
