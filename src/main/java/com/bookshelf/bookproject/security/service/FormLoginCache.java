package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.common.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormLoginCache {
    private final AccountRepository accountRepository;

    @Cacheable(value = "accountInfo", key = "#accountId", cacheManager = "cacheManagerWith1Hour")
    @Transactional(readOnly = true)
    public Account getAccount(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }

    @Cacheable(value = "roleInfo", key = "#accountId", cacheManager = "cacheManagerWith24Hours")
    @Transactional(readOnly = true)
    public List<String> getRoles(String accountId) {
        return accountRepository.findRolesByAccountId(accountId);
    }
}
