package dwuthk.search.common.model;

import org.springframework.http.HttpStatus;


public record ExceptionResponse(HttpStatus status, String message) {
}
