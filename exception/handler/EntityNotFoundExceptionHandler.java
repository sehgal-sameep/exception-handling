package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityNotFoundExceptionHandler extends AbstractApiExceptionHandler {

    public EntityNotFoundExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper,
                                          ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof EntityNotFoundException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        ApiErrorResponse response = new ApiErrorResponse(getHttpStatus(exception, HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage());
        Map<String, Object> nestedCause = new HashMap<>();
        nestedCause.put("Filename :", exception.getStackTrace()[0].getFileName());
        nestedCause.put("Class :", exception.getStackTrace()[0].getClassName());
        String method = exception.getStackTrace()[0].getMethodName();
        nestedCause.put("Method :", method);
        nestedCause.put("Line number :", exception.getStackTrace()[0].getLineNumber());
        Throwable cause = exception.getCause();
        if (cause != null) {
            nestedCause.put("Cause :", cause.getMessage());
        }
        response.addErrorProperty("details", nestedCause);
        return response;
    }
}
