package com.sameep.project.exception.starter;

public record ApiGlobalError(String code, String message) {

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
