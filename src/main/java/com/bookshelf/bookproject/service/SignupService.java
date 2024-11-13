package com.bookshelf.bookproject.service;

import com.bookshelf.bookproject.controller.dto.BankInfo;
import com.bookshelf.bookproject.controller.dto.SignupSeller;
import com.bookshelf.bookproject.controller.dto.Signup;
import com.bookshelf.bookproject.domain.*;
import com.bookshelf.bookproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {
    private final RoleRepository roleRepository;
    private final RoleManagementRepository roleManagementRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankRepository bankRepository;
    private final BankAccountRepository bankAccountRepository;

    @Transactional(readOnly = true)
    public boolean isEnableUsername(String username) {
        return accountRepository.findByAccountId(username) == null;
    }

    /**
     * 주어진 {@link Signup} 객체를 저장하고 {@code ROLE_USER} 권한을 부여합니다.
     * <p> 이 메서드는 {@link Signup} 객체를 {@link User} 엔티티로 변환한 후,
     * 해당 사용자에게 {@code ROLE_USER} 권한을 부여하고 저장합니다.
     * 권한 부여 과정은 {@link Role} 엔티티를 통해 {@link User} 엔티티에 연결됩니다.
     *
     * @param signup 저장할 {@link Signup} 객체
     *                   <p> {@code SignupUser} 객체는 사용자 가입을 위한 DTO로,
     *                   {@link User} 엔티티에 변환되어 저장됩니다.
     */
    @Transactional
    public void saveUserAccount(Signup signup) {
        User user = toUser(signup);
        saveWithRole(user, "ROLE_USER");
    }

    @Transactional
    public void saveSellerAccount(SignupSeller signupSeller) {
        Seller seller = toSeller(signupSeller);
        saveWithRole(seller, "ROLE_SELLER");
        BankInfo bankInfo = signupSeller.getBankInfo();
        saveBankInfo(bankInfo, seller);
    }

    private void saveBankInfo(BankInfo bankInfo, Seller seller) {
        Bank bank = getBankByName(bankInfo.getName());
        linkBank(bank, seller, bankInfo);
    }

    private void saveWithRole(Account account, String roleType) {
        accountRepository.save(account);
        Role role = getRoleByType(roleType);
        linkRole(role, account);
    }

    /**
     * 주어진 {@link Account} 객체와 {@link Role} 객체 간의 관계를 매핑하고,
     * 이를 {@link RoleManagement} 객체를 통해 저장합니다.
     * <p> 이 메서드는 {@link RoleManagement} 객체를 생성하여 {@link Account}와 {@link Role} 간의
     * 관계를 나타내는 연결 정보를 저장합니다. {@link RoleManagement} 객체는 {@link Account}와
     * {@link Role} 간의 중간 엔티티 역할을 하며, 이후 {@link Account} 객체에 해당 관계를 추가합니다.
     *
     * @param role {@link Role} 객체로, 사용자에게 부여할 역할 정보
     * @param account {@link Account} 객체로, 역할을 부여할 사용자
     */
    private void linkRole(Role role, Account account) {
        RoleManagement roleManagement = new RoleManagement(role, account);
        roleManagementRepository.save(roleManagement);
        account.addRoleManagement(roleManagement);
    }

    private void linkBank(Bank bank, Seller seller, BankInfo bankInfo) {
        BankAccount bankAccount = new BankAccount(seller, bank, bankInfo.getDepositor(), bankInfo.getAccountNumber());
        bankAccountRepository.save(bankAccount);
        seller.addBankAccount(bankAccount);
    }

    /**
     * 주어진 권한 유형에 해당하는 {@link Role} 객체를 반환
     * <p> 이 메서드는 {@link RoleRepository}를 사용하여 {@code type}에 해당하는 {@link Role} 객체를
     * 데이터베이스에서 조회합니다. 해당 권한을 가진 {@link Role} 객체를 찾지 못하면 {@code null}을 반환할 수 있습니다.
     *
     * @param type 조회할 권한의 유형 (예: "ROLE_USER", "ROLE_MANAGER" 등)
     * @return {@code type}에 해당하는 {@link Role} 객체, 해당 권한을 가진 {@link Role}이 없으면 {@code null}
     */
    private Role getRoleByType(String type) {
        return roleRepository.findByType(type);
    }

    private Bank getBankByName(String bankName) {
        return bankRepository.findByName(bankName);
    }

    /**
     * 주어진 {@link Signup} 객체를 {@link User} 엔티티로 변환하여 반환
     * <p> 주어진 {@link Signup} 객체를 {@link User} 엔티티로 변환하며,
     * {@code password}는 암호화하여 {@link User} 엔티티로 변환합니다.
     *
     * @param signup 변환할 {@link Signup} 객체
     * @return 변환된 {@link User} 엔티티
     */
    private User toUser(Signup signup) {
        return User.builder()
                .name(signup.getName())
                .accountId(signup.getUsername())
                .password(passwordEncoder.encode(signup.getPassword()))
                .email(signup.getEmailAddress())
                .phone(signup.getPhoneNumber())
                .build();
    }

    private Seller toSeller(SignupSeller signupSeller) {
        return Seller.builder()
                .name(signupSeller.getName())
                .accountId(signupSeller.getUsername())
                .password(passwordEncoder.encode(signupSeller.getPassword()))
                .email(signupSeller.getEmailAddress())
                .phone(signupSeller.getPhoneNumber())
                .csPhone(signupSeller.getCsPhoneNumber())
                .build();
    }

    @Cacheable("bankNames")
    @Transactional(readOnly = true)
    public List<String> getBankNames() {
        return bankRepository.findAllBankNames();
    }
}
