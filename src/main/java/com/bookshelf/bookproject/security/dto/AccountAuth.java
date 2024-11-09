package com.bookshelf.bookproject.security.dto;

import com.bookshelf.bookproject.domain.AccountStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;

@Builder
@EqualsAndHashCode
@Getter
public class AccountAuth implements CredentialsContainer {
    private final String name;
    private final String accountId;
    private String password;
    private final AccountStatus status;

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
