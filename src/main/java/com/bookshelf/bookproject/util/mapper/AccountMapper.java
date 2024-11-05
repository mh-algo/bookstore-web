package com.bookshelf.bookproject.util.mapper;

import com.bookshelf.bookproject.security.dto.AccountAuthDto;
import com.bookshelf.bookproject.domain.Account;

public class AccountMapper {
    public static AccountAuthDto toAccountAuthDto(Account account) {
        return AccountAuthDto.builder()
                .name(account.getName())
                .accountId(account.getAccountId())
                .password(account.getPassword())
                .status(account.getStatus())
                .build();
    }
}
