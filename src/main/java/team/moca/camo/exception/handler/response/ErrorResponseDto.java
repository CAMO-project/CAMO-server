package team.moca.camo.exception.handler.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Getter
public class ErrorResponseDto {

    private final String errorCode;
    private final String message;
    private final String uri;
    private final LocalDateTime timeStamp;

    @Builder
    protected ErrorResponseDto(String errorCode, String message, String uri, LocalDateTime timeStamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.uri = uri;
        this.timeStamp = timeStamp;
    }

    public static ErrorResponseDto of(BusinessException exception, HttpServletRequest request) {
        return ErrorResponseDto.builder()
                .errorCode(exception.getErrorCode())
                .message(exception.getMessage())
                .uri(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponseDto of(String errorCode, String message, HttpServletRequest request) {
        return ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(message)
                .uri(request.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
