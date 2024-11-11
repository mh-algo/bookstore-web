package com.bookshelf.bookproject.service;

import com.bookshelf.bookproject.controller.dto.SignupUser;
import com.bookshelf.bookproject.domain.Role;
import com.bookshelf.bookproject.domain.RoleManagement;
import com.bookshelf.bookproject.domain.User;
import com.bookshelf.bookproject.repository.AccountRepository;
import com.bookshelf.bookproject.repository.RoleManagementRepository;
import com.bookshelf.bookproject.repository.RoleRepository;
import com.bookshelf.bookproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final RoleRepository roleRepository;
    private final RoleManagementRepository roleManagementRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean isEnableUsername(String username) {
        return accountRepository.findByAccountId(username) == null;
    }

    @Transactional
    public void saveUserAccount(SignupUser signupUser) {
        Role roleUser = getRoleByType("ROLE_USER");
        User user = toUser(signupUser);
        assignRoleToUser(roleUser, user);
        userRepository.save(user);
    }

    private void assignRoleToUser(Role role, User user) {
        RoleManagement roleManagement = new RoleManagement(role, user);
        roleManagementRepository.save(roleManagement);
        user.addRoleManagement(roleManagement);
    }

    private Role getRoleByType(String type) {
        return roleRepository.findByType(type);
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
