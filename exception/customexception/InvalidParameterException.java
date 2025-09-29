package com.sameep.project.exception.customexception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends RuntimeException {

    private final HttpStatus httpStatus;

    public InvalidParameterException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

