package com.sameep.project.exception.handler;

import com.sameep.project.exception.customexception.FieldAlreadyExistsException;
import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FieldAlreadyExistsExceptionHandler extends AbstractApiExceptionHandler {

    public FieldAlreadyExistsExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper,
                                              ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof FieldAlreadyExistsException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        FieldAlreadyExistsException ex = (FieldAlreadyExistsException) exception;

        HttpStatus status = ex.getStatus();
        String errorCode = getErrorCode(exception);
        String errorMessage = getErrorMessage(exception);

        return new ApiErrorResponse(status, errorCode, errorMessage + " " + ex.getMessage());
    }
}
