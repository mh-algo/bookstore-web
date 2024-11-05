package com.bookshelf.bookproject.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountDeletedException extends AuthenticationException {
    public AccountDeletedException(String message) {
        super(message);
    }

    public AccountDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
