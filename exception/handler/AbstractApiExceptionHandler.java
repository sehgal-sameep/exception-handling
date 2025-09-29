package com.sameep.project.exception.handler;


import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiExceptionHandler;
import org.springframework.http.HttpStatus;

public abstract class AbstractApiExceptionHandler implements ApiExceptionHandler {
    protected final HttpStatusMapper httpStatusMapper;
    protected final ErrorCodeMapper errorCodeMapper;
    protected final ErrorMessageMapper errorMessageMapper;

    
    protected AbstractApiExceptionHandler(HttpStatusMapper httpStatusMapper,
                                       ErrorCodeMapper errorCodeMapper,
                                       ErrorMessageMapper errorMessageMapper) {
        this.httpStatusMapper = httpStatusMapper;
        this.errorCodeMapper = errorCodeMapper;
        this.errorMessageMapper = errorMessageMapper;
    }

    protected HttpStatus getHttpStatus(Throwable exception, HttpStatus defaultHttpStatus) {
        return httpStatusMapper.getHttpStatus(exception, defaultHttpStatus);
    }

    protected String getErrorCode(Throwable exception) {
        return errorCodeMapper.getErrorCode(exception);
    }

    protected String getErrorMessage(Throwable exception) {
        return errorMessageMapper.getErrorMessage(exception);
    }
}
