package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

/**
 * {@link HttpMessageNotReadableException}. This typically happens when Spring can't properly
 * decode the incoming request to JSON.
 */


@Slf4j
@Component
public class HttpMessageNotReadableExceptionHandler extends AbstractApiExceptionHandler {
    public HttpMessageNotReadableExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                  ErrorCodeMapper errorCodeMapper,
                                                  ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof HttpMessageNotReadableException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        if (getErrorMessage(exception).contains("Special characters not allowed")) {
            return new ApiErrorResponse(getHttpStatus(exception, HttpStatus.BAD_REQUEST),
                    getErrorCode(exception),
                    exception.getMessage().split("[:;]")[1]);
        } else {
            return new ApiErrorResponse(getHttpStatus(exception, HttpStatus.BAD_REQUEST),
                    getErrorCode(exception),
                    getErrorMessage(exception));
        }
    }

}
