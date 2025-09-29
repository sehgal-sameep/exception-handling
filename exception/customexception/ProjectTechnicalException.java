package com.sameep.project.exception.customexception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class ProjectTechnicalException extends RuntimeException {

    String conclusion;
    HttpStatus status;
    Map<String, Object> properties;

    public ProjectTechnicalException(String message) {
        super(message);
    }

    public ProjectTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectTechnicalException(String message, String conclusions) {
        super(message);
        conclusion = conclusions;
    }

    public ProjectTechnicalException(HttpStatus httpStatus, String message, String conclusions) {
        super(message);
        status = httpStatus;
        conclusion = conclusions;
    }


    public ProjectTechnicalException(HttpStatus httpStatus, String message) {
        super(message);
        status = httpStatus;
    }

    public ProjectTechnicalException(HttpStatus httpStatus, String message, Map<String, Object> errorproperties) {
        super(message);
        status = httpStatus;
        properties = errorproperties;
    }


}
