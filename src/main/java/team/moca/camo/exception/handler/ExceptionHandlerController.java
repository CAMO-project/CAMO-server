package team.moca.camo.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.FieldError;
import team.moca.camo.exception.handler.response.ErrorResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> businessExceptionHandler(
            BusinessException exception, HttpServletRequest request
    ) {
        log.error("Business Error >>> ", exception);
        return ResponseEntity.status(exception.getHttpStatus())
                .body(ErrorResponseDto.of(exception, request));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> argumentInvalidExceptionHandler(
            MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        log.error("Argument Error >>> ", exception);
        String message = Objects.requireNonNull(exception.getFieldError())
                .getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.of(FieldError.INVALID_FIELD.getErrorCode(), message, request));
    }
}
