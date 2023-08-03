package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthenticationError implements CamoError {

    USER_AUTHENTICATION_FAIL("C401", "회원 인증에 실패하였습니다.");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    AuthenticationError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
