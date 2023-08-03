package team.moca.camo.controller.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseDto<T> {

    private final LocalDateTime timeStamp;
    private final String message;
    private final T body;

    protected ResponseDto(LocalDateTime timeStamp, String message, T body) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.body = body;
    }

    public static <T> ResponseDto<T> of(String message) {
        return new ResponseDto<>(LocalDateTime.now(), message, null);
    }

    public static <T> ResponseDto<T> of(T body) {
        return new ResponseDto<>(LocalDateTime.now(), null, body);
    }

    public static <T> ResponseDto<T> of(T body, String message) {
        return new ResponseDto<>(LocalDateTime.now(), message, body);
    }
}
