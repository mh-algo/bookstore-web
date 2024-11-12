package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("S")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_seller_accounts"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seller extends Account {
    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 20, nullable = false)
    private String csPhone;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BankAccount> bankAccounts = new ArrayList<>();

    @Builder
    public Seller(String name, String accountId, String password, AccountStatus status, String email, String phone, String csPhone) {
        super(name, accountId, password, status);
        this.email = email;
        this.phone = phone;
        this.csPhone = csPhone;
    }
}
