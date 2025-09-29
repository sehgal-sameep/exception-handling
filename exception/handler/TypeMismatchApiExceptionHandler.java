package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@Component
public class TypeMismatchApiExceptionHandler extends AbstractApiExceptionHandler {
	public TypeMismatchApiExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper,
										   ErrorMessageMapper errorMessageMapper) {
		super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
	}

	@Override
	public boolean canHandle(Throwable exception) {
		return exception instanceof TypeMismatchException;
	}

	@Override
	public ApiErrorResponse handle(Throwable exception) {
		ApiErrorResponse response = new ApiErrorResponse(getHttpStatus(exception, HttpStatus.BAD_REQUEST),
				getErrorCode(exception), getErrorMessage(exception));
		TypeMismatchException ex = (TypeMismatchException) exception;
		response.addErrorProperty("property", getPropertyName(ex));
		response.addErrorProperty("rejectedValue", ex.getValue());
		Class<?> requiredType= ex.getRequiredType();
		response.addErrorProperty("expectedType", requiredType!= null ? requiredType.getName() : null);
		return response;
	}

	private String getPropertyName(TypeMismatchException exception) {
		if (exception instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
			return methodArgumentTypeMismatchException.getName();
		} else {
			return exception.getPropertyName();
		}
	}
}
