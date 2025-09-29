package com.sameep.project.exception.starter;

public record ApiParameterError(String code, String parameter, String message, Object rejectedValue) {

    public String getCode() {
        return code;
    }

    public String getParameter() {
        return parameter;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
