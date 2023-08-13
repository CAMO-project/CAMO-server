package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientRequestError implements CamoError {

    NON_EXISTENT_CAFE("C4001", "존재하지 않는 카페입니다."),
    UNSUPPORTED_FILE_EXTENSION("C4002", "지원하지 않는 파일 확장자명입니다. (지원: jpg, jpeg, png)");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    ClientRequestError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
