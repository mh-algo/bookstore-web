package com.bookshelf.bookproject.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.bookshelf.bookproject.domain.AccountStatus.DELETED;
import static com.bookshelf.bookproject.domain.AccountStatus.INACTIVE;

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

    // 계정 정지
    @Override
    public boolean isEnabled() {
        return account.getStatus() != INACTIVE;
    }

    // 계정 삭제
    public boolean isDeleted() {
        return account.getStatus() == DELETED;
    }
}
