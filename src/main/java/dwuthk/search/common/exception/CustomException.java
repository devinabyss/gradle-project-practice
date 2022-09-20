package dwuthk.search.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }


    public CustomException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

}
