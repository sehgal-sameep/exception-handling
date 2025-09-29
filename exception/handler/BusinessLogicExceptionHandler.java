package com.sameep.project.exception.handler;

import com.sameep.project.exception.customexception.projectFunctionalException;
import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class BusinessLogicExceptionHandler extends AbstractApiExceptionHandler {


    public BusinessLogicExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper, ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof projectFunctionalException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        projectFunctionalException FPAFunctionalException = (projectFunctionalException) exception;
        Map<String, Object> property = new HashMap<>();
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.toString(),
                FPAFunctionalException.getMessage());
        if (Objects.nonNull(FPAFunctionalException.getStatus())) {
            errorResponse = new ApiErrorResponse(FPAFunctionalException.getStatus(),
                    FPAFunctionalException.getStatus().toString(), FPAFunctionalException.getMessage());
        }
        if (Objects.nonNull(FPAFunctionalException.getConclusion())) {
            property.put("conclusion", FPAFunctionalException.getConclusion());
            errorResponse.addErrorProperties(property);
        } else if (Objects.nonNull(FPAFunctionalException.getProperties())) {
            errorResponse.addErrorProperties(FPAFunctionalException.getProperties());
        }
        return errorResponse;
    }
}
