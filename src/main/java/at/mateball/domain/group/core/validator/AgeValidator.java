package at.mateball.domain.group.core.validator;

import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AgeValidator {
    private final UserRepository userRepository;

    public AgeValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAgeWithinRange(Long baseUserId, Integer targetBirthYear) {
        Integer baseBirthYear = userRepository.findById(baseUserId)
                .map(User::getBirthYear)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.NOT_ALLOWED_AGE));

        if (baseBirthYear == null || targetBirthYear == null) {
            return false;
        }

        int thisYear = LocalDate.now().getYear();
        int baseAge = thisYear - baseBirthYear + 1;
        int targetAge = thisYear - targetBirthYear + 1;

        return Math.abs(baseAge - targetAge) <= 5;
    }
}
