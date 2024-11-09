package com.bookshelf.bookproject.service;

import com.bookshelf.bookproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final AccountRepository accountRepository;

    public boolean isEnableUsername(String username) {
        return accountRepository.findByAccountId(username) == null;
    }
}
