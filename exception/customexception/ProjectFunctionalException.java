package com.sameep.project.exception.customexception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class ProjectFunctionalException extends RuntimeException implements Serializable {

    String conclusion;
    HttpStatus status;
    Map<String, Object> properties;

    public ProjectFunctionalException(String message) {
        super(message);
    }

    public ProjectFunctionalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectFunctionalException(String message, String conclusions) {
        super(message);
        conclusion = conclusions;
    }

    public ProjectFunctionalException(HttpStatus httpStatus, String message, String conclusions) {
        super(message);
        status = httpStatus;
        conclusion = conclusions;
    }


    public ProjectFunctionalException(HttpStatus httpStatus, String message) {
        super(message);
        status = httpStatus;
    }

    public ProjectFunctionalException(HttpStatus httpStatus, String message, Map<String, Object> errorproperties) {
        super(message);
        status = httpStatus;
        properties = errorproperties;
    }


}
