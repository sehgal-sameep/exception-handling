package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmptyResultDataAccessExceptionHandler extends AbstractApiExceptionHandler {
    public EmptyResultDataAccessExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper,
                                                 ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return false;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        log.error(exception.getMessage());
        String originalMessage = exception.getMessage();
        String entityName = originalMessage.substring(originalMessage.lastIndexOf('.') + 1).trim().split(" ")[0];

        String finalMessage = entityName + " is not exist for id: " + originalMessage.split(" ")[6];
        return new ApiErrorResponse(HttpStatus.NOT_FOUND, "Record not exist", finalMessage);
    }
}
