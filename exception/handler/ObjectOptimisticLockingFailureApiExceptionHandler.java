package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ObjectOptimisticLockingFailureApiExceptionHandler extends AbstractApiExceptionHandler {

    public ObjectOptimisticLockingFailureApiExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                             ErrorCodeMapper errorCodeMapper,
                                                             ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ObjectOptimisticLockingFailureException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        ApiErrorResponse response = new ApiErrorResponse(getHttpStatus(exception, HttpStatus.CONFLICT),
                getErrorCode(exception),
                getErrorMessage(exception));
        ObjectOptimisticLockingFailureException ex = (ObjectOptimisticLockingFailureException) exception;
        response.addErrorProperty("identifier", ex.getIdentifier());
        response.addErrorProperty("persistentClassName", ex.getPersistentClassName());
        return response;
    }
}
