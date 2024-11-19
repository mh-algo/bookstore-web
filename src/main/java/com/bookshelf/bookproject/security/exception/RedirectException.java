package com.bookshelf.bookproject.security.exception;

public class RedirectException extends RuntimeException {
    public RedirectException(String message) {
        super(message);
    }

    public RedirectException(String message, Throwable cause) {
        super(message, cause);
    }
}
