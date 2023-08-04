package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthenticationError implements CamoError {

    USER_AUTHENTICATION_FAIL("C0001", "회원 인증에 실패하였습니다."),
    INVALID_TOKEN_ERROR("C0002", "유효하지 않은 토큰입니다."),
    EMAIL_DUPLICATION("C0003", "중복된 이메일입니다."),
    PHONE_DUPLICATION("C0004", "중복된 전화번호입니다."),
    NICKNAME_DUPLICATION("C0005", "중복된 닉네임입니다."),
    PASSWORD_CHECK_MISMATCH("C0006", "비밀번호 확인이 일치하지 않습니다.");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    AuthenticationError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
