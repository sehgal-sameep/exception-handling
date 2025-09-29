package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import com.sameep.project.exception.starter.ApiFieldError;
import com.sameep.project.exception.starter.ApiGlobalError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Class to handle {@link MethodArgumentNotValidException} exceptions. This is typically
 * used when `@Valid` is used on {@link org.springframework.web.bind.annotation.RestController}
 * method arguments.
 */

@Slf4j
@Component
public class MethodArgumentNotValidApiExceptionHandler extends AbstractApiExceptionHandler {

    public MethodArgumentNotValidApiExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                     ErrorCodeMapper errorCodeMapper,
                                                     ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof MethodArgumentNotValidException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {

        MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
        ApiErrorResponse response = new ApiErrorResponse(getHttpStatus(exception, HttpStatus.BAD_REQUEST),
                getErrorCode(exception),
                getMessage(ex));
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasFieldErrors()) {
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ApiFieldError(getCode(fieldError),
                            fieldError.getField(),
                            getMessage(fieldError),
                            fieldError.getRejectedValue()))
                    .forEach(response::addFieldError);
        }

        if (bindingResult.hasGlobalErrors()) {
            bindingResult.getGlobalErrors().stream()
                    .map(globalError -> new ApiGlobalError(errorCodeMapper.getErrorCode(globalError.getCode()),
                            errorMessageMapper.getErrorMessage(globalError.getCode(), globalError.getDefaultMessage())))
                    .forEach(response::addGlobalError);
        }

        return response;
    }

    private String getCode(FieldError fieldError) {
        String code = fieldError.getCode();
        String fieldSpecificCode = fieldError.getField() + "." + code;
        return errorCodeMapper.getErrorCode(fieldSpecificCode, code);
    }

    private String getMessage(FieldError fieldError) {
        String code = fieldError.getCode();
        String fieldSpecificCode = fieldError.getField() + "." + code;
        return errorMessageMapper.getErrorMessage(fieldSpecificCode, code, fieldError.getDefaultMessage());
    }

    private String getMessage(MethodArgumentNotValidException exception) {
        return "Validation failed for object='" + exception.getBindingResult().getObjectName() + "'. Error count: " + exception.getBindingResult().getErrorCount();
    }
}
