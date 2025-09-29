package com.sameep.project.exception.handler;

import com.sameep.project.exception.mapper.ErrorCodeMapper;
import com.sameep.project.exception.mapper.ErrorMessageMapper;
import com.sameep.project.exception.mapper.HttpStatusMapper;
import com.sameep.project.exception.starter.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataIntegrityViolationExceptionHandler extends AbstractApiExceptionHandler {

    public DataIntegrityViolationExceptionHandler(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper,
                                                  ErrorMessageMapper errorMessageMapper) {
        super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
    }

    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof DataIntegrityViolationException;
    }

//    @Override
//    public ApiErrorResponse handle(Throwable exception) {
//        log.error(exception.getCause().getCause().getMessage());
//        String s = exception.getCause().getCause().getMessage();
//        String key = s.split("=")[0];
//        String value = s.split("=")[1];
//
//        key = key.substring(key.indexOf("(") + 1);
//        key = key.substring(0, key.indexOf(")"));
//
////        value = value.substring(value.indexOf("(") + 1);
//        String finalMessage = value;
////        value = value.substring(0, value.indexOf(")"));
////        String finalMessage = "'" + value + "' is already exist." + " Please enter different value";
//        //To give custom exception when unique constraint for resource is violated.
//        if(s.contains("resource_id_unique"))
//        {
//            finalMessage=" Resource schedule for selected resource" + " already exist." + " Please enter unique resource";
//        }
//
//        return new ApiErrorResponse(HttpStatus.CONFLICT,
//                "Data Integrity Violation for colm: " + key,
//                finalMessage);
//    }

    @Override
    public ApiErrorResponse handle(Throwable exception) {
        log.error(exception.getCause().getCause().getMessage());
        String errorMessage = exception.getCause().getCause().getMessage();

        String key = "unknown"; // Default value in case of extraction failure
        String finalMessage = ""; // Default message

        try {
            // Attempt to extract the key from the error message
            String[] parts = errorMessage.split("=");
            if (parts.length > 1) {
                // Extract the key and value safely
                key = parts[0].substring(parts[0].indexOf("(") + 1);
                key = key.substring(0, key.indexOf(")"));

                finalMessage = parts[1];
            }else{
                finalMessage = errorMessage;
            }

            if (errorMessage.contains("resource_id_unique")) {
                finalMessage = "Resource schedule for the selected resource already exists. Please enter a unique resource.";
            }

        } catch (Exception e) {
            log.warn("Could not extract details from the error message: {}", errorMessage, e);
        }

        return new ApiErrorResponse(HttpStatus.CONFLICT,
                "Data Integrity Violation for column: " + key,
                finalMessage);

    }
}










