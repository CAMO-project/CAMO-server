package team.moca.camo.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public CustomException(CustomError customError) {
        super(customError.getMessage());
        this.errorCode = customError.getErrorCode();
        this.message = customError.getMessage();
    }
}
