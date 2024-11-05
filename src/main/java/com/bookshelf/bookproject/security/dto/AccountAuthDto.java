package com.bookshelf.bookproject.security.dto;

import com.bookshelf.bookproject.domain.AccountStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class AccountAuthDto {
    private final String name;
    private final String accountId;
    private final String password;
    private final AccountStatus status;

    public AccountAuthDto clearPassword() {
        return AccountAuthDto.builder()
                .name(this.name)
                .accountId(this.accountId)
                .status(this.status)
                .build();
    }
}
