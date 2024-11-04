package com.bookshelf.bookproject.repository;


import java.util.List;

public interface CustomAccountRepository {
    List<String> findRolesByAccountId(String accountId);
}
