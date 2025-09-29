package com.sameep.project.exception.handler;

import com.sameep.project.exception.customexception.InvalidParameterException;
import com.sameep.project.exception.starter.ApiErrorResponse;
import com.sameep.project.exception.starter.ApiExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class InvalidParameterExceptionHandler implements ApiExceptionHandler {

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof InvalidParameterException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        InvalidParameterException ex = (InvalidParameterException) exception;
        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_PARAMETER",
                ex.getMessage()
        );
    }
}
