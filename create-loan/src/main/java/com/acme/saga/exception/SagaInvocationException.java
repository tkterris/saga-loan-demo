package com.acme.saga.exception;

import java.lang.RuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SagaInvocationException extends RuntimeException {
    public SagaInvocationException(String message) {
        super(message);
    }
}      