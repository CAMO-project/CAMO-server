package team.moca.camo.exception;

public class UserTypeException extends RuntimeException {
    public UserTypeException() {
        super();
    }

    public UserTypeException(String message) {
        super(message);
    }

    public UserTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTypeException(Throwable cause) {
        super(cause);
    }
}
