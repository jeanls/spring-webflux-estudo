package com.learnreactivespring.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleInternalSeverError(ServerException e) {
        log.error("Error has occurred.: ", e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
