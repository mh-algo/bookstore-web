package com.bookshelf.bookproject.service;

import com.bookshelf.bookproject.controller.dto.SignupUser;
import com.bookshelf.bookproject.domain.User;
import com.bookshelf.bookproject.repository.AccountRepository;
import com.bookshelf.bookproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isEnableUsername(String username) {
        return accountRepository.findByAccountId(username) == null;
    }

    public void saveUserAccount(SignupUser signupUser) {
        userRepository.save(toUser(signupUser));
    }

    private User toUser(SignupUser signupUser) {
        return User.builder()
                .name(signupUser.getName())
                .accountId(signupUser.getUsername())
                .password(passwordEncoder.encode(signupUser.getPassword()))
                .email(getEmail(signupUser))
                .phone(getPhoneNumber(signupUser))
                .build();
    }

    private static String getEmail(SignupUser signupUser) {
        return signupUser.getEmailId() + "@" + signupUser.getEmailAddress();
    }

    private static String getPhoneNumber(SignupUser signupUser) {
        return signupUser.getPhonePrefix() + "-" + signupUser.getPhoneMiddle() + "-" + signupUser.getPhoneLast();
    }
}
