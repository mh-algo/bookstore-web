package com.bookshelf.bookproject.security.repository.querydsl;


import java.util.List;

public interface CustomAccountRepository {
    List<String> findRolesByAccountId(String accountId);
}
