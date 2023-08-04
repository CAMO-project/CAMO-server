package team.moca.camo.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.handler.response.ErrorResponseDto;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> customExceptionHandler(
            BusinessException exception, HttpServletRequest request
    ) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(ErrorResponseDto.of(exception, request));
    }
}
