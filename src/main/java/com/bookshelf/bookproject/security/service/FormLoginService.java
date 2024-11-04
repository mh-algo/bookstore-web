package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.domain.dto.AccountDto;
import com.bookshelf.bookproject.domain.entity.Account;
import com.bookshelf.bookproject.domain.mapper.AccountMapper;
import com.bookshelf.bookproject.repository.AccountRepository;
import com.bookshelf.bookproject.security.dto.FormUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class FormLoginService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountRepository.findByAccountId(username);

        if (user == null) {
            throw new UsernameNotFoundException("Failed to find user '" + username + "'");
        }

        AccountDto accountDto = AccountMapper.toAccountDto(user);

        List<String> roles = accountRepository.findRolesByAccountId(username);
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role)).toList();

        return new FormUser(accountDto, authorities);
    }
}
