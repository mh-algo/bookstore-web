package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.domain.*;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.Signup;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.SignupSeller;
import com.bookshelf.bookproject.publicpage.controller.dto.signup.item.BankInfo;
import com.bookshelf.bookproject.publicpage.repository.BankAccountRepository;
import com.bookshelf.bookproject.publicpage.repository.BankRepository;
import com.bookshelf.bookproject.publicpage.repository.RoleManagementRepository;
import com.bookshelf.bookproject.publicpage.repository.RoleRepository;
import com.bookshelf.bookproject.common.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {
    private final AccountCache accountCache;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RoleManagementRepository roleManagementRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankRepository bankRepository;
    private final BankAccountRepository bankAccountRepository;

    public boolean isEnableUsername(String username) {
        return getAccount(username) == null;
    }

    private Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }

    /**
     * 주어진 {@link Signup} 객체를 저장하고 {@code ROLE_USER} 권한을 부여
     * <p> 이 메서드는 {@link Signup} 객체를 {@link User} 엔티티로 변환한 후,
     * 해당 사용자에게 {@code ROLE_USER} 권한을 부여하고 저장합니다.
     * 권한 부여 과정은 {@link Role} 엔티티를 통해 {@link User} 엔티티에 연결됩니다.
     *
     * @param signup 저장할 {@link Signup} 객체 (회원 가입 정보)
     */
    @CacheEvict(value = "account:accountInfo", key = "#signup.username", cacheResolver = "cacheResolver")
    @Transactional
    public void saveUserAccount(Signup signup) {
        User user = toUser(signup);
        saveWithRole(user, "ROLE_USER");
    }

    /**
     * 판매자 가입 정보를 저장하고 {@code ROLE_SELLER} 권한을 부여합니다.
     * <p> {@link SignupSeller} 객체를 {@link Seller} 엔티티로 변환한 후 저장하고,
     * 해당 판매자에게 {@code ROLE_SELLER} 권한을 부여하여 {@link Seller}와 {@link Role} 간의 관계를 설정합니다.
     * 또한, {@link SignupSeller}에 포함된 {@link BankInfo}를 {@link BankAccount} 엔티티로 변환하여 저장하고,
     * {@link Seller}와 {@link Bank} 간의 관계를 설정합니다.
     *
     * @param signupSeller 저장할 {@link SignupSeller} 객체 (판매자 가입 정보)
     */
    @CacheEvict(value = "account:accountInfo", key = "#signupSeller.username", cacheResolver = "cacheResolver")
    @Transactional
    public void saveSellerAccount(SignupSeller signupSeller) {
        Seller seller = toSeller(signupSeller);
        saveWithRole(seller, "ROLE_SELLER");

        Bank bank = getBankByName(signupSeller.getBankInfo().getName());
        linkBank(bank, seller, signupSeller.getBankInfo());
    }

    /**
     * {@link Account} 엔티티를 저장
     * <p> 이 메서드는 {@link Account} 엔티티를 저장한 후,
     * 해당 계정에 지정된 {@code roleType}에 따라 {@link Role}과의 관계를 설정합니다.
     *
     * @param account 저장할 {@link Account} 엔티티
     * @param roleType 부여할 {@link Role}의 유형
     */
    private void saveWithRole(Account account, String roleType) {
        accountRepository.save(account);
        Role role = getRoleByType(roleType);
        linkRole(role, account);
    }


    /**
     * 주어진 {@link Account} 객체와 {@link Role} 객체 간의 관계를 매핑하고,
     * 이를 {@link RoleManagement} 객체를 통해 저장
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

    /**
     * 판매자와 은행 간의 관계를 {@link BankAccount} 객체로 저장
     * <p> 이 메서드는 주어진 {@link Seller}와 {@link Bank} 객체를 사용해 {@link BankAccount} 객체를 생성하고 저장합니다.
     * {@link BankAccount}는 {@link Seller}와 {@link Bank} 간의 중간 관계 엔티티로서, 판매자의 은행 계좌 정보를 나타냅니다.
     * 또한, 생성된 {@link BankAccount} 객체를 {@link Seller} 객체에 추가합니다.
     *
     * @param bank 판매자의 은행 정보를 나타내는 {@link Bank} 객체
     * @param seller 계좌를 연결할 판매자 {@link Seller} 객체
     * @param bankInfo 저장할 계좌 정보를 담고 있는 {@link BankInfo} 객체
     */
    private void linkBank(Bank bank, Seller seller, BankInfo bankInfo) {
        BankAccount bankAccount = new BankAccount(seller, bank, bankInfo.getDepositor(), bankInfo.getAccountNumber());
        bankAccountRepository.save(bankAccount);
        seller.addBankAccount(bankAccount);
    }

    /**
     * 주어진 권한 유형에 해당하는 {@link Role} 객체를 반환
     * <p> 이 메서드는 {@link RoleRepository}를 사용하여 {@code type}에 해당하는 {@link Role} 객체를
     * 조회합니다. 해당 권한을 가진 {@link Role} 객체를 찾지 못하면 {@code null}을 반환할 수 있습니다.
     *
     * @param type 조회할 권한의 유형
     * @return {@code type}에 해당하는 {@link Role} 객체, 해당 권한을 가진 {@link Role}이 없으면 {@code null}
     */
    private Role getRoleByType(String type) {
        return roleRepository.findByType(type);
    }

    /**
     * 주어진 은행 이름에 해당하는 {@link Bank} 객체를 반환
     * <p> 이 메서드는 {@link BankRepository}를 사용하여 {@code bankName}에 해당하는 {@link Bank} 객체를
     * 조회합니다. 해당 권한을 가진 {@link Bank} 객체를 찾지 못하면 {@code null}을 반환할 수 있습니다.
     *
     * @param bankName 조회할 은행 이름
     * @return {@code bankName}에 해당하는 {@link Bank} 객체, 해당 이름을 가진 {@link Bank}가 없으면 {@code null}
     */
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

    /**
     * 주어진 {@link SignupSeller} 객체를 {@link Seller} 엔티티로 변환하여 반환
     * <p> 주어진 {@link SignupSeller} 객체를 {@link Seller} 엔티티로 변환하며,
     * {@code password}는 암호화하여 {@link Seller} 엔티티로 변환합니다.
     *
     * @param signupSeller 변환할 {@link SignupSeller} 객체
     * @return 변환된 {@link Seller} 엔티티
     */
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

    /**
     * 은행 이름을 조회하여 리스트로 반환
     * <p> 이 메서드는 은행 이름을 전부 조회한 후 리스트로 반환하며,
     * 최초 실행 이후에는 반환값을 캐시에 저장하여 이후 호출 시 캐시된 값을 반환합니다.
     *
     * @return 조회한 은행 이름 리스트
     */
    @Cacheable(value = "bankNames", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public List<String> getBankNames() {
        return bankRepository.findAllBankNames();
    }
}
