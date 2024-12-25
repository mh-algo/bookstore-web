package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.common.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bookshelf.bookproject.config.CacheConstants.ACCOUNT;
import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;

@Service
@RequiredArgsConstructor
public class FormLoginCache {
    private final AccountCache accountCache;
    private final AccountRepository accountRepository;

    public Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }

    @Cacheable(value = ACCOUNT + ":roleInfo", key = "#accountId", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public List<String> getRoles(String accountId) {
        return accountRepository.findRolesByAccountId(accountId);
    }
}
