package team.moca.camo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.moca.camo.exception.error.CamoError;

@Getter
public class CamoException extends RuntimeException {

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    public CamoException(CamoError camoError) {
        this.errorCode = camoError.getErrorCode();
        this.message = camoError.getMessage();
        this.httpStatus = camoError.getHttpStatus();
    }
}
