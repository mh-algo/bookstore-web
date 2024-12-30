package com.bookshelf.bookproject.common.exception;

public class UnAuthenticationException extends RuntimeException {
    public UnAuthenticationException(String message) {
        super(message);
    }

    public UnAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
