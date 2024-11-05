package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository {
    Account findByAccountId(String accountId);
}
