package at.mateball.domain.group.core.validator;

import at.mateball.domain.group.core.Group;
import at.mateball.domain.group.core.GroupStatus;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;

public class GroupValidator {

    public static void validate(Group group) {
        if (GroupStatus.from(group.getStatus()) == GroupStatus.COMPLETED) {
            throw new BusinessException(BusinessErrorCode.ALREADY_COMPLETED_GROUP);
        }
    }
}
