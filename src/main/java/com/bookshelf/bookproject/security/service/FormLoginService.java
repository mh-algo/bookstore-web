package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.security.dto.AccountAuth;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.security.repository.AccountRepository;
import com.bookshelf.bookproject.security.dto.FormUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class FormLoginService implements UserDetailsService {
    private final FormLoginCache formLoginCache;

    public FormLoginService(FormLoginCache formLoginCache) {
        this.formLoginCache = formLoginCache;
    }

    /**
     * 주어진 사용자 이름에 해당하는 사용자 정보를 조회하고, {@link UserDetails} 타입의 객체로 반환
     * <p> 주어진 사용자 이름에 해당하는 사용자 정보를 조회합니다.
     * 조회된 사용자 정보가 없을 경우 {@link UsernameNotFoundException} 예외를 던지고,
     * 사용자 정보가 존재할 경우 사용자 정보와 권한을 포함한 {@link UserDetails} 타입의 객체를 반환합니다.
     *
     * @param username 조회할 사용자 이름
     * @return 사용자 정보와 권한을 담은 {@link UserDetails} 객체
     * @throws UsernameNotFoundException 조회된 사용자 정보가 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = formLoginCache.getAccount(username);

        if (user == null) {
            throw new UsernameNotFoundException("Failed to find user '" + username + "'");
        }

        AccountAuth accountAuth = toAccountAuth(user);

        List<String> roles = formLoginCache.getRoles(username);
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role)).toList();

        return new FormUser(accountAuth, authorities);
    }

    /**
     * {@link Account}를 {@link AccountAuth}로 변환
     *
     * @param account 변환할 {@link Account} 객체
     * @return 변환된 {@link AccountAuth} 객체
     */
    private static AccountAuth toAccountAuth(Account account) {
        return AccountAuth.builder()
                .name(account.getName())
                .accountId(account.getAccountId())
                .password(account.getPassword())
                .status(account.getStatus())
                .build();
    }
}
