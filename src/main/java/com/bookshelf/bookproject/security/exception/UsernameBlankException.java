package com.bookshelf.bookproject.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 사용자 이름이 빈칸일 경우 발생
 *
 * <p>인증 처리 로직에서 사용자 이름이 빈칸임을 감지하고,
 * 사용자에게 사용자 이름 에러 메시지를 제공할 때 사용합니다.
 */
public class UsernameBlankException extends AuthenticationException {
    public UsernameBlankException(String message) {
        super(message);
    }

    public UsernameBlankException(String message, Throwable cause) {
        super(message, cause);
    }
}