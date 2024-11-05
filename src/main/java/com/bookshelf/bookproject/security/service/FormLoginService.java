package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.security.dto.AccountAuthDto;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.util.mapper.AccountMapper;
import com.bookshelf.bookproject.repository.AccountRepository;
import com.bookshelf.bookproject.security.dto.FormUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class FormLoginService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public FormLoginService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountRepository.findByAccountId(username);

        if (user == null) {
            throw new UsernameNotFoundException("Failed to find user '" + username + "'");
        }

        AccountAuthDto accountDto = AccountMapper.toAccountAuthDto(user);

        List<String> roles = accountRepository.findRolesByAccountId(username);
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role)).toList();

        return new FormUser(accountDto, authorities);
    }
}
