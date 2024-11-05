package com.bookshelf.bookproject.security.exception;

import org.springframework.security.core.AuthenticationException;

public class UsernameBlankException extends AuthenticationException {
    public UsernameBlankException(String message) {
        super(message);
    }

    public UsernameBlankException(String message, Throwable cause) {
        super(message, cause);
    }
}