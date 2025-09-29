package com.sameep.project.exception.starter;


public record ApiFieldError(String code, String property, String message, Object rejectedValue) {
	
    public String getCode() {
        return code;
    }

    public String getProperty() {
        return property;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
