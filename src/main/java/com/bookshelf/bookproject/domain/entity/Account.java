package com.bookshelf.bookproject.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts", uniqueConstraints =
        @UniqueConstraint(name = "unique_account_id", columnNames = "account_id")
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", length = 10)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Account extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_name", nullable = false, length = 20)
    private String name;
    @Column(name = "account_id", nullable = false, length = 20)
    private String accountId;
    @Column(name = "pwd", nullable = false, length = 60)
    private String password;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;


    public Account(String name, String accountId, String password, AccountStatus status) {
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.status = status;
    }
}
