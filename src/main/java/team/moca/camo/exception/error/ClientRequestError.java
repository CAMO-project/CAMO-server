package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClientRequestError implements CamoError {

    NON_EXISTENT_CAFE("C4001", "존재하지 않는 카페입니다."),
    UNSUPPORTED_FILE_EXTENSION("C4002", "지원하지 않는 파일 확장자명입니다. (지원: jpg, jpeg, png)"),
    TAGS_MAXIMUM_NUMBER_EXCEEDS("C4003", "최대 태그 개수를 초과하였습니다. (최대 3개)"),
    DUPLICATE_TAGS("C4004", "중복된 태그입니다.");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    ClientRequestError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
