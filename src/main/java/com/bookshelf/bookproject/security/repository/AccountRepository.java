package com.bookshelf.bookproject.security.repository;

import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.security.repository.querydsl.CustomAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository {
    Account findByAccountId(String accountId);
}
