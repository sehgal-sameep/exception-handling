package com.sameep.project.exception.starter;

public interface FallbackApiExceptionHandler {
    ApiErrorResponse handle(Throwable exception);
}
