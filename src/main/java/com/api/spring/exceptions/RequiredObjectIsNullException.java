package com.api.spring.exceptions;

public class RequiredObjectIsNullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RequiredObjectIsNullException(String message) {
        super(message);
    }
    public RequiredObjectIsNullException() {
        super("It is not allowed to persist a null object!");
    }
}
