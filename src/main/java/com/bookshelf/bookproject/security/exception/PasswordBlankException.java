package com.bookshelf.bookproject.security.exception;

import org.springframework.security.core.AuthenticationException;

public class PasswordBlankException extends AuthenticationException {
    public PasswordBlankException(String message) {
        super(message);
    }

    public PasswordBlankException(String message, Throwable cause) {
        super(message, cause);
    }
}