package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;


@Slf4j
@Component
public class FileNotFoundExceptionHandler extends AbstractApiExceptionHandler {

	public FileNotFoundExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper,
										ErrorMessageMapper errorMessageMapper) {
		super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
	}

	@Override
	public boolean canHandle(Throwable exception) {
		return exception instanceof FileNotFoundException;
	}

	@Override
	public ApiErrorResponse handle(Throwable exception) {
		return new ApiErrorResponse(getHttpStatus(exception, HttpStatus.INTERNAL_SERVER_ERROR),
                "File which you want to access is not available ",
                " FileNotFoundExceptionHandler ");
	}

}
