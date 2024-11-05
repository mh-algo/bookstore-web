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
            throw new AccountDeletedException(username);
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
