package team.moca.camo.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthenticationError implements CamoError {

    USER_AUTHENTICATION_FAIL("01", "회원 인증에 실패하였습니다."),
    INVALID_TOKEN("02", "유효하지 않은 토큰입니다."),
    EMAIL_DUPLICATION("03", "중복된 이메일입니다."),
    PHONE_DUPLICATION("04", "중복된 전화번호입니다."),
    NICKNAME_DUPLICATION("05", "중복된 닉네임입니다."),
    PASSWORD_CHECK_MISMATCH("06", "비밀번호 확인이 일치하지 않습니다."),
    KAKAO_ACCOUNT_ALREADY_INTEGRATED("07", "해당 카카오 계정은 이미 연동되어 있습니다.");

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    AuthenticationError(String errorCode, String message) {
        this.errorCode = "C00" + errorCode;
        this.message = message;
    }
}
