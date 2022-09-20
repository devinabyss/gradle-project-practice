package dwuthk.search.common.controller;

import dwuthk.search.common.exception.CustomException;
import dwuthk.search.common.model.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class DefaultControllerAdvisor {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e, WebRequest webRequest) {
        log.info("## Invalid Request Case : {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
        return new ResponseEntity<>(new ExceptionResponse(e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
    }


}
