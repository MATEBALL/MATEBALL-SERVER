package at.mateball.domain.group.core.validator;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateValidator {
    private DateValidator() {

    }

    public static void validate(LocalDate date) {
        LocalDate today = LocalDate.now();
//        경기가 없는 월요일을 선택한 경우
//        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
//            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_MONDAY);
//        }

        if (date.isBefore(today)) {
            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_PAST);
        }

//        경기 이틀전 매칭을 생성하려는 경우
//        LocalDate minAvailableDate = today.plusDays(2);
//        if (minAvailableDate.getDayOfWeek() == DayOfWeek.MONDAY) {
//            minAvailableDate = today.plusDays(2);
//            minAvailableDate = today.plusDays(3);
//        }

//        if (date.isBefore(minAvailableDate)) {
//            throw new BusinessException(BusinessErrorCode.BAD_REQUEST_DATE);
//        }
    }
}
