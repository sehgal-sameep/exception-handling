package com.sameep.project.exception.mapper;

import com.sameep.project.exception.starter.ErrorHandlingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class contains the logic for getting the matching HTTP Status for the
 * given {@link Throwable}.
 */
@Slf4j
@Component
public class HttpStatusMapper {
	private final ErrorHandlingProperties properties;

	public HttpStatusMapper(@Qualifier("ErrorProperties") ErrorHandlingProperties properties) {
		this.properties = properties;
	}

	public HttpStatus getHttpStatus(Throwable exception) {
		return getHttpStatus(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public HttpStatus getHttpStatus(Throwable exception, HttpStatus defaultHttpStatus) {
		HttpStatus status = getHttpStatusFromPropertiesOrAnnotation(exception.getClass());
		if (status != null) {
			return status;
		}

		if (exception instanceof ResponseStatusException responseStatusException) {
			log.info("Is instance of ResponseStatusException");
			return (HttpStatus) responseStatusException.getStatusCode();
		}
		log.info("defaultHttpStatus: {}", defaultHttpStatus);
		return defaultHttpStatus;
	}

	private HttpStatus getHttpStatusFromPropertiesOrAnnotation(Class<?> exceptionClass) {
		if (exceptionClass == null) {
			return null;
		}
		String exceptionClassName = exceptionClass.getName();
		if (properties.getHttpStatuses().containsKey(exceptionClassName)) {
			return properties.getHttpStatuses().get(exceptionClassName);
		}

		ResponseStatus responseStatus = AnnotationUtils.getAnnotation(exceptionClass, ResponseStatus.class);
		if (responseStatus != null) {
			return responseStatus.value();
		}

		if (properties.isSearchSuperClassHierarchy()) {
			return getHttpStatusFromPropertiesOrAnnotation(exceptionClass.getSuperclass());
		} else {
			return null;
		}
	}

}
