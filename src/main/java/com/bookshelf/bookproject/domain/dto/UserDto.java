package com.bookshelf.bookproject.domain.dto;

import com.bookshelf.bookproject.domain.entity.AccountStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class UserDto {
    private final String name;
    private final String accountId;
    private final String password;
    private final AccountStatus status;
    private final String email;
    private final String phone;
}
