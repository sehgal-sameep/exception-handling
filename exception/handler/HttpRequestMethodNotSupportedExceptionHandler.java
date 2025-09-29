package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@Slf4j
@Component
public class HttpRequestMethodNotSupportedExceptionHandler extends AbstractApiExceptionHandler {

    public HttpRequestMethodNotSupportedExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                         ErrorCodeMapper errorCodeMapper,
                                                         ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof HttpRequestMethodNotSupportedException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        return new ApiErrorResponse(getHttpStatus(exception, HttpStatus.METHOD_NOT_ALLOWED),
                getErrorCode(exception),
                getErrorMessage(exception));
    }
}
