package com.bookshelf.bookproject.util.mapper;

import com.bookshelf.bookproject.security.dto.AccountAuthDto;
import com.bookshelf.bookproject.domain.Account;

public class AccountMapper {
    /**
     * {@link Account}를 {@link AccountAuthDto}로 변환
     *
     * @param account 변환할 {@link Account} 객체
     * @return 변환된 {@link AccountAuthDto} 객체
     */
    public static AccountAuthDto toAccountAuthDto(Account account) {
        return AccountAuthDto.builder()
                .name(account.getName())
                .accountId(account.getAccountId())
                .password(account.getPassword())
                .status(account.getStatus())
                .build();
    }
}
