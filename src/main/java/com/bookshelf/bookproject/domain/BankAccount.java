package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String depositor;

    @Column(name = "account_number", length = 20, nullable = false)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="seller_id", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_bank_account_seller"))
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="bank_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bank_account_bank"))
    private Bank bank;
}
