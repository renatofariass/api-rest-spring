package com.api.spring.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String messagem) {
        super(messagem);
    }
}
