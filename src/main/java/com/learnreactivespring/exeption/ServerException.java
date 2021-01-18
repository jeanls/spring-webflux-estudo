package com.learnreactivespring.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServerException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ServerException(String message) {
        super(message);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
