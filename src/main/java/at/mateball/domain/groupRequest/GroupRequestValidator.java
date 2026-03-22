package at.mateball.domain.groupRequest;


import at.mateball.domain.group.api.dto.GroupValidationRes;
import at.mateball.domain.group.api.dto.RequestValidationRes;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import org.springframework.stereotype.Component;

import static at.mateball.domain.group.core.validator.DateValidator.validate;

@Component
public class GroupRequestValidator {

    public void validateCreateRequest(Long userId, GroupValidationRes group, RequestValidationRes data) {
        validate(group.gameDate());

        if (group.leaderId().equals(userId)) {
            throw new BusinessException(BusinessErrorCode.CANNOT_REQUEST_OWN_MATCH);
        }

        if (group.status() == GroupStatus.COMPLETED.getValue()) {
            throw new BusinessException(BusinessErrorCode.ALREADY_FINISHED_MATCH);
        }

        if (data.isDuplicatedRequest()) {
            throw new BusinessException(BusinessErrorCode.DUPLICATED_MATCH_REQUEST);
        }

        if (data.hasPendingRequest()) {
            throw new BusinessException(BusinessErrorCode.ALREADY_HAS_PENDING_REQUEST);
        }
    }
}
