package com.bookshelf.bookproject.security.provider;

import com.bookshelf.bookproject.security.dto.FormUser;
import com.bookshelf.bookproject.security.exception.AccountDeletedException;
import com.bookshelf.bookproject.security.exception.PasswordBlankException;
import com.bookshelf.bookproject.security.exception.UsernameBlankException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class FormLoginProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public FormLoginProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 주어진 인증 정보를 사용해 검증을 수행하고, 조회된 사용자 정보를 {@link Authentication} 타입 객체로 반환합니다.
     *
     * @param authentication 사용자 인증 요청 객체
     * @return 인증된 사용자 정보를 담은 {@link Authentication} 객체
     * @throws AuthenticationException 인증에 실패할 경우 발생
     * @throws UsernameBlankException 주어진 인증 정보의 사용자 이름이 빈칸일 경우 발생
     * @throws PasswordBlankException 주어진 인증 정보의 비밀번호가 빈칸일 경우 발생
     * @throws DisabledException 조회된 사용자가 비활성 상태일 경우 발생
     * @throws AccountDeletedException 조회된 사용자가 삭제된 상태일 경우 발생
     * @throws BadCredentialsException 주어진 인증 정보의 비밀번호가 조회된 사용자 정보와 일치하지 않을 경우 발생
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (username.isBlank()) {
            throw new UsernameBlankException("Username cannot be blank");
        }

        if (password.isBlank()) {
            throw new PasswordBlankException("Password cannot be blank");
        }

        FormUser loadedUser = (FormUser) userDetailsService.loadUserByUsername(username);

        if (!loadedUser.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        if (loadedUser.isDeleted()) {
            throw new AccountDeletedException("Account is deleted");
        }

        // 비밀번호 암호화시 수정 필요 (현재 평문 저장)
        if (!password.equals(loadedUser.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new UsernamePasswordAuthenticationToken(loadedUser.getAccountDto().clearPassword(), null, loadedUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
