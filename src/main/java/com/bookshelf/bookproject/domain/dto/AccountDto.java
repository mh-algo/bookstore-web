package com.bookshelf.bookproject.domain.dto;

import com.bookshelf.bookproject.domain.entity.AccountStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class AccountDto {
    private final String name;
    private final String accountId;
    private final String password;
    private final AccountStatus status;

    public AccountDto clearPassword() {
        return AccountDto.builder()
                .name(this.name)
                .accountId(this.accountId)
                .status(this.status)
                .build();
    }
}
