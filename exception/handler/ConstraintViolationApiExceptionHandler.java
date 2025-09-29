package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import com.sameep.project.exception.starter.ApiFieldError;
import com.sameep.project.exception.starter.ApiGlobalError;
import com.sameep.project.exception.starter.ApiParameterError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;


@Slf4j
@Component
public class ConstraintViolationApiExceptionHandler extends AbstractApiExceptionHandler {

    public ConstraintViolationApiExceptionHandler(HttpStatusMapper httpStatusMapper,
                                                  ErrorCodeMapper errorCodeMapper,
                                                  ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof ConstraintViolationException;
    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {

        ConstraintViolationException ex = (ConstraintViolationException) exception;
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT,
                getErrorCode(exception),
                getMessage(ex));
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        violations.stream()
                .map(constraintViolation -> {
                    Optional<Path.Node> leafNode = getLeafNode(constraintViolation.getPropertyPath());
                    if (leafNode.isPresent()) {
                        Path.Node node = leafNode.get();
                        ElementKind elementKind = node.getKind();
                        if (elementKind == ElementKind.PROPERTY) {
                            return new ApiFieldError(getCode(constraintViolation),
                                    node.toString(),
                                    getMessage(constraintViolation),
                                    constraintViolation.getInvalidValue());
                        } else if (elementKind == ElementKind.BEAN) {
                            return new ApiGlobalError(getCode(constraintViolation),
                                    getMessage(constraintViolation));
                        } else if (elementKind == ElementKind.PARAMETER) {
                            return new ApiParameterError(getCode(constraintViolation),
                                    node.toString(),
                                    getMessage(constraintViolation),
                                    constraintViolation.getInvalidValue());
                        } else {
                            log.warn("Unable to convert constraint violation with element kind {}: {}", elementKind, constraintViolation);
                            return null;
                        }
                    } else {
                        log.warn("Unable to convert constraint violation: {}", constraintViolation);
                        return null;
                    }
                })
                .forEach(error -> {
                    if (error instanceof ApiFieldError apiFieldError) {
                        response.addFieldError(apiFieldError);
                    } else if (error instanceof ApiGlobalError apiGlobalError) {
                        response.addGlobalError(apiGlobalError);
                    } else if (error instanceof ApiParameterError apiParameterError) {
                        response.addParameterError(apiParameterError);
                    }
                });
        return response;
    }

    private Optional<Path.Node> getLeafNode(Path path) {
        return StreamSupport.stream(path.spliterator(), false).reduce((a, b) -> b);
    }

    private String getCode(ConstraintViolation<?> constraintViolation) {
        String code = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String fieldSpecificCode = constraintViolation.getPropertyPath().toString() + "." + code;
        return errorCodeMapper.getErrorCode(fieldSpecificCode, code);
    }

    private String getMessage(ConstraintViolation<?> constraintViolation) {
        String code = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String fieldSpecificCode = constraintViolation.getPropertyPath().toString() + "." + code;
        return errorMessageMapper.getErrorMessage(fieldSpecificCode, code, constraintViolation.getMessage());
    }

    private String getMessage(ConstraintViolationException exception) {
        return "Validation failed. Error count: " + exception.getConstraintViolations().size();
    }
}
