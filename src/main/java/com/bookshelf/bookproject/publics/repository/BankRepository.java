package com.bookshelf.bookproject.publics.repository;

import com.bookshelf.bookproject.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    @Query("select b.name from Bank b")
    List<String> findAllBankNames();

    Bank findByName(String name);
}
