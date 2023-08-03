package team.moca.camo.exception;

import lombok.Getter;

@Getter
public enum AuthenticationError implements CustomError {

    USER_AUTHENTICATION_FAIL("C401", "회원 인증에 실패하였습니다.");

    private final String errorCode;
    private final String message;

    AuthenticationError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
