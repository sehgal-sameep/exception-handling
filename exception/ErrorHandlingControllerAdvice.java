package com.sameep.project.exception;


import com.sameep.project.exception.starter.ApiErrorResponse;
import com.sameep.project.exception.starter.ApiExceptionHandler;
import com.sameep.project.exception.starter.ErrorHandlingProperties;
import com.sameep.project.exception.starter.FallbackApiExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@Primary
@Slf4j
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private final ErrorHandlingProperties properties;
    private final List<ApiExceptionHandler> handlers;
    private final FallbackApiExceptionHandler fallbackHandler;

    public ErrorHandlingControllerAdvice(@Qualifier("ErrorProperties") ErrorHandlingProperties properties,
                                         List<ApiExceptionHandler> handlers,
                                         FallbackApiExceptionHandler fallbackHandler) {
        this.properties = properties;
        this.handlers = handlers;
        this.fallbackHandler = fallbackHandler;
        this.handlers.sort(AnnotationAwareOrderComparator.INSTANCE);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Throwable exception, WebRequest webRequest, Locale locale) {
        logException(exception);
        ApiErrorResponse errorResponse = null;
        for (ApiExceptionHandler handler : handlers) {
            if (handler.canHandle(exception)) {
                errorResponse = handler.handle(exception);
                break;
            }
        }
        if (errorResponse == null) {
            errorResponse = fallbackHandler.handle(exception);
        }

        return ResponseEntity.status(errorResponse.getHttpStatus())
                .body(errorResponse);
    }

    private void logException(Throwable exception) {
        if (properties.getFullStacktraceClasses().contains(exception.getClass())) {
            log.error(exception.getClass().getName(), exception);
        } else {
            switch (properties.getExceptionLogging()) {
                case WITH_STACKTRACE:
                    log.error(exception.getClass().getName(), exception);
                    break;
                case MESSAGE_ONLY:
                    log.error(exception.getClass().getName(), exception);
                    break;
                case NO_LOGGING:
                    break;
                default:
                    break;
            }
        }
    }
}
