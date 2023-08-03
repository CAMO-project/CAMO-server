package team.moca.camo.exception.error;

import org.springframework.http.HttpStatus;

public interface CamoError {

    String getErrorCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
