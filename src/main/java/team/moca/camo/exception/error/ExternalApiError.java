package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExternalApiError implements CamoError {

    KAKAO_API_REQUEST_FAIL("01", "카카오 API 요청이 실패하였습니다."),
    EMPTY_RESULT("02", "API 응답 결과가 비어있습니다.");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    ExternalApiError(String errorCode, String message) {
        this.errorCode = "C99" + errorCode;
        this.message = message;
    }
}
