package com.bookshelf.bookproject.common;

import com.bookshelf.bookproject.common.repository.AccountRepository;
import com.bookshelf.bookproject.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookshelf.bookproject.config.CacheConstants.ACCOUNT;
import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;

@Service
@RequiredArgsConstructor
public class AccountCache {
    private final AccountRepository accountRepository;

    @Cacheable(value = ACCOUNT + ":accountInfo", key = "#accountId", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public Account getAccount(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }
}
