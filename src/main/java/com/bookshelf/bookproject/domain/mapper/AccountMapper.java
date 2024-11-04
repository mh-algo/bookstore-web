package com.bookshelf.bookproject.domain.mapper;

import com.bookshelf.bookproject.domain.dto.AccountDto;
import com.bookshelf.bookproject.domain.entity.Account;

public class AccountMapper {
    public static AccountDto toAccountDto(Account account) {
        return AccountDto.builder()
                .name(account.getName())
                .accountId(account.getAccountId())
                .password(account.getPassword())
                .status(account.getStatus())
                .build();
    }
}
