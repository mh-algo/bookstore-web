package com.bookshelf.bookproject.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 사용자가 삭제된 상태일 경우 발생
 *
 * <p>인증 처리 로직에서 사용자가 삭제된 상태임을 감지하고,
 * 사용자에게 로그인 에러 메시지를 제공할 때 사용합니다.
 */
public class AccountDeletedException extends AuthenticationException {
    public AccountDeletedException(String message) {
        super(message);
    }

    public AccountDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
