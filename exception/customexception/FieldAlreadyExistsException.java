package com.sameep.project.exception.customexception;

import org.springframework.http.HttpStatus;

public class FieldAlreadyExistsException extends RuntimeException {
    private final HttpStatus status;

    public FieldAlreadyExistsException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public FieldAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
