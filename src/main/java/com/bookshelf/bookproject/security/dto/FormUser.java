package com.bookshelf.bookproject.security.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.bookshelf.bookproject.domain.AccountStatus.DELETED;
import static com.bookshelf.bookproject.domain.AccountStatus.INACTIVE;

public class FormUser implements UserDetails {
    private final AccountAuth account;
    private final List<GrantedAuthority> authorities;

    /**
     * {@link UserDetails} 인터페이스를 구현한 FormUser 객체를 생성합니다.
     * <p>주어진 사용자 정보와 권한 리스트를 기반으로 객체가 생성됩니다.
     *
     * @param account 조회된 사용자의 정보를 담은 {@link AccountAuth} 객체
     * @param authorities 사용자에게 부여된 권한 목록
     */
    public FormUser(AccountAuth account, List<GrantedAuthority> authorities) {
        this.account = account;
        this.authorities = authorities;
    }

    public AccountAuth getAccountAuth() {
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

    /**
     * 계정 활성화 상태 확인
     *
     * @return 계정이 활성화 된 경우 true, 그렇지 않을 경우 false
     */
    @Override
    public boolean isEnabled() {
        return account.getStatus() != INACTIVE;
    }

    /**
     * 계정 삭제 여부 확인
     *
     * @return 계정이 삭제되었을 경우 true, 그렇지 않을 경우 false
     */
    public boolean isDeleted() {
        return account.getStatus() == DELETED;
    }
}
