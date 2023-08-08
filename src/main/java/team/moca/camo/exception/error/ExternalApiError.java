package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExternalApiError implements CamoError {

    KAKAO_API_REQUEST_FAIL("C9901", "카카오 API 요청이 실패하였습니다."),
    EMPTY_RESULT("C9902", "API 응답 결과가 비어있습니다.");

    ExternalApiError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
