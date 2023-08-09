package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FieldError implements CamoError {

    INVALID_FIELD("C1001", "필드 유효성 검증에 실패하였습니다."),
    INVALID_PAGE("C1002", "유효하지 않은 페이지입니다.");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    FieldError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
