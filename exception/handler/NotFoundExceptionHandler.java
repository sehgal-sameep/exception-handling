package com.sameep.project.exception.handler;

import com.sameep.project.exception.customexception.NotFoundException;
import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import org.springframework.http.HttpStatus;

public class NotFoundExceptionHandler extends AbstractApiExceptionHandler {


    public NotFoundExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper, ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof NotFoundException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        return new ApiErrorResponse(HttpStatus.NOT_FOUND,"Not Found" ,
                exception.getMessage());
    }
}
