package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HibernetConstraintViolationException extends AbstractApiExceptionHandler{



    public HibernetConstraintViolationException(HttpStatusMapper
                                                        httpStatusMapper, ErrorCodeMapper errorCodeMapper, ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ConstraintViolationException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        return new ApiErrorResponse(HttpStatus.CONFLICT,"Validation_Failed" ,
                "Unique Constrain Voilation");
    }
}
