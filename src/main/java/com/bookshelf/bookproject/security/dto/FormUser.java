package com.bookshelf.bookproject.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.bookshelf.bookproject.domain.AccountStatus.ACTIVE;

public class FormUser implements UserDetails {
    private final AccountAuthDto account;
    private final List<GrantedAuthority> authorities;

    public FormUser(AccountAuthDto account, List<GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    public AccountAuthDto getAccountDto() {
        return account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getAccountId();
    }

    // 계정 정지, 계정 탈퇴시 인증 x
    @Override
    public boolean isEnabled() {
        return account.getStatus() == ACTIVE;
    }
}
