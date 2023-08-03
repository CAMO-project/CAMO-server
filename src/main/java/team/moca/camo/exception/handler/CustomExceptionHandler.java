package team.moca.camo.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.moca.camo.exception.CamoException;
import team.moca.camo.exception.handler.response.ErrorResponseDto;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CamoException.class)
    protected ResponseEntity<ErrorResponseDto> customExceptionHandler(
            CamoException exception, HttpServletRequest request
    ) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(ErrorResponseDto.of(exception, request));
    }
}
