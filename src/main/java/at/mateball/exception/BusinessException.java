package at.mateball.exception;
public class BusinessException extends RuntimeException {
    private final BusinessErrorCode businessErrorCode;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        super(businessErrorCode.getMessage());
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessErrorCode getErrorCode() {
        return businessErrorCode;
    }
}

