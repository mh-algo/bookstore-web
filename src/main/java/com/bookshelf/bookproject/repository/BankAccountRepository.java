package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
