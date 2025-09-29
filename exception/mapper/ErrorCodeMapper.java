package com.sameep.project.exception.mapper;

import com.sameep.project.exception.starter.ErrorHandlingProperties;
import com.sameep.project.exception.starter.ResponseErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * This class contains the logic for getting the matching error code for the given {@link Throwable}.
 */
@Component
public class ErrorCodeMapper {

    private final ErrorHandlingProperties properties;

    public ErrorCodeMapper(@Qualifier("ErrorProperties") ErrorHandlingProperties properties) {
        this.properties = properties;
    }

    public String getErrorCode(Throwable exception) {
        String code = getErrorCodeFromPropertiesOrAnnotation(exception.getClass());
        if (code != null) {
            return code;
        }
        switch (properties.getDefaultErrorCodeStrategy()) {
            case FULL_QUALIFIED_NAME:
                return exception.getClass().getName();
            case ALL_CAPS:
                return convertToAllCaps(exception.getClass().getSimpleName());
            default:
                throw new IllegalArgumentException("Unknown default error code strategy: " + properties.getDefaultErrorCodeStrategy());
        }
    }

    public String getErrorCode(String fieldSpecificErrorCode, String errorCode) {
        if (properties.getCodes().containsKey(fieldSpecificErrorCode)) {
            return properties.getCodes().get(fieldSpecificErrorCode);
        }

        return getErrorCode(errorCode);
    }

    public String getErrorCode(String errorCode) {
        if (properties.getCodes().containsKey(errorCode)) {
            return properties.getCodes().get(errorCode);
        }

        return errorCode;
    }

    private String convertToAllCaps(String exceptionClassName) {
        String result = exceptionClassName.replaceFirst("Exception$", "");
        result = result.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase(Locale.ENGLISH);
        return result;
    }

    private String getErrorCodeFromPropertiesOrAnnotation(Class<?> exceptionClass) {
        if (exceptionClass == null) {
            return null;
        }
        String exceptionClassName = exceptionClass.getName();
        if (properties.getCodes().containsKey(exceptionClassName)) {
            return properties.getCodes().get(exceptionClassName);
        }
        ResponseErrorCode errorCodeAnnotation = AnnotationUtils.getAnnotation(exceptionClass, ResponseErrorCode.class);
        if (errorCodeAnnotation != null) {
            return errorCodeAnnotation.value();
        }

        if (properties.isSearchSuperClassHierarchy()) {
            return getErrorCodeFromPropertiesOrAnnotation(exceptionClass.getSuperclass());
        } else {
            return null;
        }
    }

}
