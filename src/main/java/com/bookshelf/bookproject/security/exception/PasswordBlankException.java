package com.bookshelf.bookproject.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 비밀번호가 빈칸일 경우 발생
 *
 * <p>인증 처리 로직에서 비밀번호가 빈칸임을 감지하고,
 * 사용자에게 비밀번호 에러 메시지를 제공할 때 사용합니다.
 */
public class PasswordBlankException extends AuthenticationException {
    public PasswordBlankException(String message) {
        super(message);
    }

    public PasswordBlankException(String message, Throwable cause) {
        super(message, cause);
    }
}